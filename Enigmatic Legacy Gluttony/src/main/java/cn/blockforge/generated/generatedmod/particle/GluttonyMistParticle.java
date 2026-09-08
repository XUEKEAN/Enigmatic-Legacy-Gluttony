package cn.blockforge.generated.generatedmod.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.SimpleParticleType;

public class GluttonyMistParticle extends TextureSheetParticle {
    private final SpriteSet sprites;

    protected GluttonyMistParticle(ClientLevel level, double x, double y, double z,
                                   double xSpeed, double ySpeed, double zSpeed,
                                   SpriteSet sprites, float size, float alpha,
                                   float r, float g, float b) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);
        this.sprites = sprites;
        this.xd = xSpeed;
        this.yd = ySpeed;
        this.zd = zSpeed;
        // 使用原版必存在的白色柔圆贴图，这里统一染成浅血红，杜绝紫黑/缺材质方块。
        this.quadSize = size * (0.8F + this.random.nextFloat() * 0.4F);
        this.lifetime = 14 + this.random.nextInt(9);
        this.hasPhysics = false;
        this.gravity = 0.0F;
        this.setColor(r, g, b);
        this.setAlpha(alpha);
        this.setSpriteFromAge(sprites);
    }

    @Override
    public void tick() {
        super.tick();
        this.setSpriteFromAge(this.sprites);
        this.quadSize *= 0.965F;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public static Provider mistProvider(SpriteSet sprites) {
        // 薄血雾：较小的浅红柔圆波点
        return new Provider(sprites, 0.22F, 0.6F, 1.0F, 0.38F, 0.33F);
    }

    public static Provider auraProvider(SpriteSet sprites) {
        // 玩家周身暗红气息：更大更淡的柔圆
        return new Provider(sprites, 0.5F, 0.55F, 0.95F, 0.22F, 0.19F);
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;
        private final float size;
        private final float alpha;
        private final float r;
        private final float g;
        private final float b;

        public Provider(SpriteSet sprites, float size, float alpha, float r, float g, float b) {
            this.sprites = sprites;
            this.size = size;
            this.alpha = alpha;
            this.r = r;
            this.g = g;
            this.b = b;
        }

        @Override
        public TextureSheetParticle createParticle(SimpleParticleType type, ClientLevel level,
                                                   double x, double y, double z,
                                                   double xSpeed, double ySpeed, double zSpeed) {
            return new GluttonyMistParticle(level, x, y, z, xSpeed, ySpeed, zSpeed,
                    this.sprites, this.size, this.alpha, this.r, this.g, this.b);
        }
    }
}
