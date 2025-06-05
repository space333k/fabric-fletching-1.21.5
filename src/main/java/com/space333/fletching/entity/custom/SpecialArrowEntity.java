package com.space333.fletching.entity.custom;

import com.space333.fletching.Fletching;
import com.space333.fletching.entity.ModEntityType;
import com.space333.fletching.item.ModItems;
import com.space333.fletching.util.ComponentHelper;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.EndermiteEntity;
import net.minecraft.entity.passive.FoxEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.PositionFlag;
import net.minecraft.particle.EntityEffectParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.*;
import net.minecraft.world.GameRules;
import net.minecraft.world.TeleportTarget;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static com.space333.fletching.util.ArrowEffect.*;

public class SpecialArrowEntity extends PersistentProjectileEntity {
	private static final int MAX_POTION_DURATION_TICKS = 600;
	private static final int NO_POTION_COLOR = -1;
	private static final TrackedData<Integer> COLOR = DataTracker.registerData(SpecialArrowEntity.class, TrackedDataHandlerRegistry.INTEGER);
	private static final byte PARTICLE_EFFECT_STATUS = 0;

	private long chunkTicketExpiryTicks = 0L;
	public int floatingTimer = 0;
	public int FLOATING_LIMIT = 200;

	private List<String> effects = List.of(DEFAULT, DEFAULT, DEFAULT);

	public SpecialArrowEntity(EntityType<? extends SpecialArrowEntity> entityType, World world) {
		super(entityType, world);
	}

	public SpecialArrowEntity(World world, double x, double y, double z, ItemStack stack, @Nullable ItemStack shotFrom) {
		super(ModEntityType.SPECIAL_ARROW, x, y, z, world, stack, shotFrom);
		this.effects = ComponentHelper.getComponents(this.getItemStack());
		this.initColor();
		setStrength();
	}

	public SpecialArrowEntity(World world, LivingEntity owner, ItemStack stack, @Nullable ItemStack shotFrom) {
		super(ModEntityType.SPECIAL_ARROW, owner, world, stack, shotFrom);
		this.effects = ComponentHelper.getComponents(this.getItemStack());
		this.initColor();
		setStrength();
	}

	private PotionContentsComponent getPotionContents() {
		return this.getItemStack().getOrDefault(DataComponentTypes.POTION_CONTENTS, PotionContentsComponent.DEFAULT);
	}

	private float getPotionDurationScale() {
		return this.getItemStack().getOrDefault(DataComponentTypes.POTION_DURATION_SCALE, 1.0F);
	}

	private void setPotionContents(PotionContentsComponent potionContentsComponent) {
		this.getItemStack().set(DataComponentTypes.POTION_CONTENTS, potionContentsComponent);
		this.initColor();
	}

	@Override
	protected void setStack(ItemStack stack) {
		super.setStack(stack);
		this.initColor();
		setStrength();
	}

	public void setStrength() {
		if(hasEffect(TIER2)) {
			this.setDamage(4);
		}
		if(hasEffect(TIER3)) {
			this.setDamage(6);
		}
		if(hasEffect(TIER4)) {
			this.setDamage(10);
		}
		if(hasEffect(LIGHTWEIGHT)) {
			this.setDamage(1);
		}
	}

	private void initColor() {
		PotionContentsComponent potionContentsComponent = this.getPotionContents();
		this.dataTracker.set(COLOR, potionContentsComponent.equals(PotionContentsComponent.DEFAULT) ? -1 : potionContentsComponent.getColor());
	}

	public void addEffect(StatusEffectInstance effect) {
		this.setPotionContents(this.getPotionContents().with(effect));
	}

	@Override
	protected void initDataTracker(DataTracker.Builder builder) {
		super.initDataTracker(builder);
		builder.add(COLOR, -1);
	}

	@Override
	public void tick() {
		int x = ChunkSectionPos.getSectionCoordFloored(this.getPos().getX());
		int y = ChunkSectionPos.getSectionCoordFloored(this.getPos().getZ());

		if(hasEffect(PHASING)) {
			phasingLogic();
		}
		else {
			super.tick();
		}


		if (this.getWorld().isClient) {
			if (this.isInGround()) {
				if (this.inGroundTime % 5 == 0) {
					this.spawnParticles(1);
				}
			} else {
				this.spawnParticles(2);
			}
		} else if (this.isInGround() && this.inGroundTime != 0 && !this.getPotionContents().equals(PotionContentsComponent.DEFAULT) && this.inGroundTime >= 600) {
			this.getWorld().sendEntityStatus(this, (byte)0);
			this.setStack(new ItemStack(ModItems.CUSTOM_ARROW));
		}

		if (this.isAlive()) {
			BlockPos blockPos = BlockPos.ofFloored(this.getPos());
			if ((--this.chunkTicketExpiryTicks <= 0L || x != ChunkSectionPos.getSectionCoord(blockPos.getX()) || y != ChunkSectionPos.getSectionCoord(blockPos.getZ()))
					&& this.getOwner() instanceof ServerPlayerEntity serverPlayerEntity && hasEffect(TELEPORTING)) {
				this.chunkTicketExpiryTicks = handleTeleportingArrow();
			}
		}
	}

	public long handleTeleportingArrow() {
		if (this.getWorld() instanceof ServerWorld serverWorld) {
			ChunkPos chunkPos = this.getChunkPos();
			serverWorld.resetIdleTimeout();
			return addArrowTicket(serverWorld, chunkPos) - 1L;
		} else {
			return 0L;
		}
	}

	public static long addArrowTicket(ServerWorld world, ChunkPos chunkPos) {
		world.getChunkManager().addTicket(Fletching.ARROW_TICKET, chunkPos, 2);
		return Fletching.ARROW_TICKET.expiryTicks();
	}


	private void spawnParticles(int amount) {
		int i = this.getColor();
		if (i != -1 && amount > 0) {
			for (int j = 0; j < amount; j++) {
				this.getWorld()
						.addParticleClient(
								EntityEffectParticleEffect.create(ParticleTypes.ENTITY_EFFECT, i), this.getParticleX(0.5), this.getRandomBodyY(), this.getParticleZ(0.5), 0.0, 0.0, 0.0
						);
			}
		}
	}

	public int getColor() {
		return this.dataTracker.get(COLOR);
	}

	@Override
	protected void onHit(LivingEntity target) {
		super.onHit(target);
		Entity entity = this.getEffectCause();

		applyOnHitEffects(target);

		PotionContentsComponent potionContentsComponent = this.getPotionContents();
		float f = this.getPotionDurationScale();
		potionContentsComponent.forEachEffect(effect -> target.addStatusEffect(effect, entity), f);
	}

	@Override
	protected void onBlockHit(BlockHitResult blockHitResult) {
		super.onBlockHit(blockHitResult);
		applyOnBlockHitEffects();
	}

	public void applyOnBlockHitEffects() {
		if(hasEffect(TELEPORTING)) {
			teleport();
		}
		if(hasEffect(EXPLODING)) {
			this.getWorld().createExplosion(this, this.getX(), this.getY(), this.getZ(), 1, false, World.ExplosionSourceType.MOB);
			this.discard();
		}
		if(hasEffect(LIGHTWEIGHT)) {
			this.discard();
		}
	}

	public void applyOnHitEffects(LivingEntity target) {
		Entity entity = this.getEffectCause();
		if(hasEffect(FLAME)) {
			target.setOnFireFor(5);
		}
		if(hasEffect(KNOCKBACK)) {
			double e = Math.max(0.0, 1.0 - target.getAttributeValue(EntityAttributes.KNOCKBACK_RESISTANCE));
			Vec3d vec3d = this.getVelocity().multiply(1.0, 0.0, 1.0).normalize().multiply(1 * 0.6 * e);
			if (vec3d.lengthSquared() > 0.0) {
				target.addVelocity(vec3d.x, 0.1, vec3d.z);
			}
		}
		if(hasEffect(SPECTRAL)) {
			StatusEffectInstance statusEffectInstance = new StatusEffectInstance(StatusEffects.GLOWING, 200, 0);
			target.addStatusEffect(statusEffectInstance, entity);
		}
		if(hasEffect(EXPLODING)) {
			this.getWorld().createExplosion(this, this.getX(), this.getY(), this.getZ(), 1, false, World.ExplosionSourceType.MOB);
			this.discard();
		}
		if(hasEffect(TELEPORTING)) {
			teleport();
		}
		if(hasEffect(TELEPORTER)) {
			teleporter(target);
		}
	}

	@Override
	protected ProjectileDeflection hitOrDeflect(HitResult hitResult) {
		if(!hasEffect(BOUNCING) || hitResult.getType() == HitResult.Type.ENTITY) {
			return super.hitOrDeflect(hitResult);
		}
		else if (hitResult.getType() == HitResult.Type.BLOCK) {
			BlockHitResult blockHitResult = (BlockHitResult)hitResult;
			BlockPos pos = blockHitResult.getBlockPos();
			Block block = this.getWorld().getBlockState(pos).getBlock();
			if(block != Blocks.TARGET) {
				this.setVelocity(this.getVelocity().multiply(0.80));
			}
			else {
				return super.hitOrDeflect(hitResult);
			}
			this.setInGround(false);
			bounce(blockHitResult);

			if(this.getVelocity().length() <= 0.05) {
				super.hitOrDeflect(hitResult);
			}

			return ProjectileDeflection.SIMPLE;
		}
		return ProjectileDeflection.NONE;
	}

	public void bounce(BlockHitResult blockHitResult) {
		Vec3d velocity = this.getVelocity();
		Vec3d normal = blockHitResult.getSide().getDoubleVector().normalize();
		Vec3d reflected = velocity.subtract(normal.multiply(2 * velocity.dotProduct(normal)));

		this.setVelocity(reflected);
	}

	@Override
	protected ItemStack getDefaultItemStack() {
		return new ItemStack(ModItems.CUSTOM_ARROW);
	}

	@Override
	public void handleStatus(byte status) {
		if (status == 0) {
			int i = this.getColor();
			if (i != -1) {
				float f = (i >> 16 & 0xFF) / 255.0F;
				float g = (i >> 8 & 0xFF) / 255.0F;
				float h = (i >> 0 & 0xFF) / 255.0F;

				for (int j = 0; j < 20; j++) {
					this.getWorld()
							.addParticleClient(
									EntityEffectParticleEffect.create(ParticleTypes.ENTITY_EFFECT, f, g, h),
									this.getParticleX(0.5),
									this.getRandomBodyY(),
									this.getParticleZ(0.5),
									0.0,
									0.0,
									0.0
							);
				}
			}
		} else {
			super.handleStatus(status);
		}
	}

	@Override
	protected double getGravity() {
		Vec3d velocity = this.getVelocity();
		if(hasEffect(FLOATING) && !(Math.abs(velocity.x) <= 0.05 && Math.abs(velocity.z) <= 0.05) && this.floatingTimer < FLOATING_LIMIT) {
			this.floatingTimer++;

			this.setVelocity(velocity.multiply(1.011));
			return 0.000;
		}
		else if(hasEffect(LIGHTWEIGHT)) {
			return super.getGravity()/2;
		}
		return super.getGravity();
	}

	@Override
	protected float getDragInWater() {
		if(hasEffect(MARINE)) {
			return 0.99F;
		}
		return super.getDragInWater();
	}

	@Override
	public byte getPierceLevel() {
		if(hasEffect(PIERCING)) {
			return 10;
		}
		return super.getPierceLevel();
	}

	public boolean hasEffect(String effect) {
		return this.effects.contains(effect);
	}

	public void teleporter(LivingEntity target){
		boolean bl = false;
		int diameter = 16;
		World world = this.getWorld();
		for (int i = 0; i < 16; i++) {
			double d = target.getX() + (target.getRandom().nextDouble() - 0.5) * diameter;
			double e = MathHelper.clamp(
					target.getY() + (target.getRandom().nextDouble() - 0.5) * diameter,
					(double)world.getBottomY(),
					(double)(world.getBottomY() + ((ServerWorld)world).getLogicalHeight() - 1)
			);
			double f = target.getZ() + (target.getRandom().nextDouble() - 0.5) * diameter;
			if (target.hasVehicle()) {
				target.stopRiding();
			}

			Vec3d vec3d = target.getPos();
			if (target.teleport(d, e, f, true)) {
				world.emitGameEvent(GameEvent.TELEPORT, vec3d, GameEvent.Emitter.of(target));
				SoundCategory soundCategory;
				SoundEvent soundEvent;
				if (target instanceof FoxEntity) {
					soundEvent = SoundEvents.ENTITY_FOX_TELEPORT;
					soundCategory = SoundCategory.NEUTRAL;
				} else {
					soundEvent = SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT;
					soundCategory = SoundCategory.PLAYERS;
				}

				world.playSound(null, target.getX(), target.getY(), target.getZ(), soundEvent, soundCategory);
				target.onLanding();
				bl = true;
				break;
			}
		}

		if (bl && target instanceof PlayerEntity playerEntity) {
			playerEntity.clearCurrentExplosion();
		}
	}

	public void teleport() {
		if (this.getWorld() instanceof ServerWorld serverWorld && !this.isRemoved()) {
			Entity owner = this.getOwner();
			if (owner != null && (!(owner instanceof LivingEntity livingEntity) ? owner.isAlive() : livingEntity.isAlive() && !livingEntity.isSleeping())) {
				Vec3d pos = this.getLastRenderPos();
				if (owner instanceof ServerPlayerEntity serverPlayerEntity) {
					if (serverPlayerEntity.networkHandler.isConnectionOpen()) {
						if (this.random.nextFloat() < 0.05F && serverWorld.getGameRules().getBoolean(GameRules.DO_MOB_SPAWNING)) {
							EndermiteEntity endermiteEntity = EntityType.ENDERMITE.create(serverWorld, SpawnReason.TRIGGERED);
							if (endermiteEntity != null) {
								endermiteEntity.refreshPositionAndAngles(owner.getX(), owner.getY(), owner.getZ(), owner.getYaw(), owner.getPitch());
								serverWorld.spawnEntity(endermiteEntity);
							}
						}

						if (this.hasPortalCooldown()) {
							owner.resetPortalCooldown();
						}

						ServerPlayerEntity serverPlayerEntity2 = serverPlayerEntity.teleportTo(
								new TeleportTarget(serverWorld, pos, Vec3d.ZERO, 0.0F, 0.0F, PositionFlag.combine(PositionFlag.ROT, PositionFlag.DELTA), TeleportTarget.NO_OP)
						);
						if (serverPlayerEntity2 != null) {
							serverPlayerEntity2.onLanding();
							serverPlayerEntity2.clearCurrentExplosion();
							serverPlayerEntity2.damage(serverPlayerEntity.getServerWorld(), this.getDamageSources().enderPearl(), 5.0F);
						}

						this.getWorld().playSound(null, pos.x, pos.y, pos.z, SoundEvents.ENTITY_PLAYER_TELEPORT, SoundCategory.PLAYERS);
					}
				} else {
					Entity entity2 = owner.teleportTo(new TeleportTarget(serverWorld, pos, owner.getVelocity(), owner.getYaw(), owner.getPitch(), TeleportTarget.NO_OP));
					if (entity2 != null) {
						entity2.onLanding();
					}

					this.getWorld().playSound(null, pos.x, pos.y, pos.z, SoundEvents.ENTITY_PLAYER_TELEPORT, SoundCategory.PLAYERS);
				}

				this.discard();
			} else {
				this.discard();
			}
		}
	}

	public void phasingLogic() {
		if (this.shake > 0) {
			this.shake--;
		}

		if (this.isTouchingWaterOrRain()) {
			this.extinguish();
		}
		this.inGroundTime = 0;

		Vec3d position = this.getPos();
		Vec3d velocity = this.getVelocity();

		if (this.isTouchingWater()) {
			this.setVelocity(velocity.multiply(this.getDragInWater()));
		}
		else {
			this.setVelocity(velocity.multiply(0.99));
		}

		if (this.isCritical()) {
			for (int i = 0; i < 4; i++) {
				this.getWorld()
						.addParticleClient(
								ParticleTypes.CRIT, position.x + velocity.x * i / 4.0, position.y + velocity.y * i / 4.0, position.z + velocity.z * i / 4.0, -velocity.x, -velocity.y + 0.2, -velocity.z
						);
			}
		}

		float f = (float)(MathHelper.atan2(velocity.x, velocity.z) * 180.0F / (float)Math.PI);
		float g = (float)(MathHelper.atan2(velocity.y, velocity.horizontalLength()) * 180.0F / (float)Math.PI);

		this.setPitch(updateRotation(this.getPitch(), g));
		this.setYaw(updateRotation(this.getYaw(), f));

		Vec3d newPosition = position.add(velocity);
		EntityHitResult entityHitResult = this.getEntityCollision(position, newPosition);

		if(entityHitResult == null) {
			this.setPosition(newPosition);
			this.tickBlockCollision();
		}
		else if (this.isAlive() && !this.noClip) {
			this.hitOrDeflect(entityHitResult);
			this.velocityDirty = true;
		}

		this.applyGravity();
		super.baseTick();
	}
}
