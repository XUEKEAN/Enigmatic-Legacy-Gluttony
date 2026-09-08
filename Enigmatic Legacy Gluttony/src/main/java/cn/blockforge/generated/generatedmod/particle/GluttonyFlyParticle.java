package cn.blockforge.generated.generatedmod.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

/**
 * 吞噬飞入粒子：被吞噬者化作的透明浅红光点，加速飞入玩家体内。
 * 自绘 tick 运动，避免叠加父类位移；接近玩家或寿命结束即消失。
 */
public class GluttonyFlyParticle extends TextureSheetParticle {
    private final SpriteSet sprites;

    protected GluttonyFlyParticle(ClientLevel level, double x, double y, double z,
                                  double xSpeed, double ySpeed, double zSpeed, SpriteSet sprites) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);
        this.sprites = sprites;
        this.xd = xSpeed;
        this.yd = ySpeed;
        this.zd = zSpeed;
        this.quadSize = 0.07F + this.random.nextFloat() * 0.05F;
        this.lifetime = 36;
        this.hasPhysics = false;
        this.gravity = 0.0F;
        this.friction = 1.0F;
        // 原版白色柔圆贴图染成浅血红，无蓝色分量，避免紫色视觉
        this.setColor(1.0F, 0.42F, 0.36F);
        this.setAlpha(0.7F);
        this.setSpriteFromAge(sprites);
    }

    @Override
    public void tick() {
        if (this.age++ >= this.lifetime) {
            this.remove();
            return;
        }
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        Player player = Minecraft.getInstance().player;
        if (player != null) {
            Vec3 target = player.getEyePosition().add(0.0, -0.2, 0.0);
            Vec3 to = target.subtract(this.x, this.y, this.z);
            if (to.lengthSqr() < 0.49) {
                this.remove();
                return;
            }
            Vec3 dir = to.normalize();
            this.xd += dir.x * 0.085;
            this.yd += dir.y * 0.085;
            this.zd += dir.z * 0.085;
            double speedSq = this.xd * this.xd + this.yd * this.yd + this.zd * this.zd;
            if (speedSq > 0.1764) {
                double scale = 0.42 / Math.sqrt(speedSq);
                this.xd *= scale;
                this.yd *= scale;
                this.zd *= scale;
            }
        }
        this.move(this.xd, this.yd, this.zd);
        float life = 1.0F - (float) this.age / (float) this.lifetime;
        this.setAlpha(Math.max(0.0F, 0.5F * life));
        this.quadSize = Math.max(0.015F, this.quadSize * 0.97F);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public static Provider provider(SpriteSet sprites) {
        return new Provider(sprites);
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Override
        public TextureSheetParticle createParticle(SimpleParticleType type, ClientLevel level,
                                                   double x, double y, double z,
                                                   double xSpeed, double ySpeed, double zSpeed) {
            return new GluttonyFlyParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, this.sprites);
        }
    }
}
