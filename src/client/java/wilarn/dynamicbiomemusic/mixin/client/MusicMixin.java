package wilarn.dynamicbiomemusic.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.MusicTracker;
import net.minecraft.sound.MusicSound;
import net.minecraft.world.biome.Biome;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.RegistryKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MusicTracker.class)
public class MusicMixin {
    private static MusicSound lastRandomMusic = null;

    @Inject(method = "isPlayingType(Lnet/minecraft/sound/MusicSound;)Z", at = @At("HEAD"), cancellable = true)
    private void onIsPlayingType(MusicSound type, CallbackInfoReturnable<Boolean> cir) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.world != null && client.player != null) {
            boolean isNether = client.world.getRegistryKey().getValue().getPath().equals("the_nether");
            
            // Create custom MusicSound instances for each dimension
            if (isNether) {
                // For Nether, use a mix of nether-like music
                MusicSound[] netherMusic = new MusicSound[]{
                    new MusicSound(net.minecraft.sound.SoundEvents.MUSIC_NETHER_BASALT_DELTAS, 12000, 24000, true),
                    new MusicSound(net.minecraft.sound.SoundEvents.MUSIC_NETHER_CRIMSON_FOREST, 12000, 24000, true),
                    new MusicSound(net.minecraft.sound.SoundEvents.MUSIC_NETHER_WARPED_FOREST, 12000, 24000, true),
                    new MusicSound(net.minecraft.sound.SoundEvents.MUSIC_NETHER_SOUL_SAND_VALLEY, 12000, 24000, true),
                    new MusicSound(net.minecraft.sound.SoundEvents.MUSIC_NETHER_NETHER_WASTES, 12000, 24000, true)
                };
                
                int randomIndex = (int)(Math.random() * netherMusic.length);
                MusicSound selectedMusic = netherMusic[randomIndex];
                boolean shouldPlay = type.equals(selectedMusic);
                
                if (shouldPlay) {
                    lastRandomMusic = selectedMusic;
                }
                
                cir.setReturnValue(shouldPlay);
            } else {
                // For Overworld, use a mix of overworld music
                MusicSound[] overworldMusic = new MusicSound[]{
                    new MusicSound(net.minecraft.sound.SoundEvents.MUSIC_GAME, 12000, 24000, true),
                    new MusicSound(net.minecraft.sound.SoundEvents.MUSIC_CREATIVE, 12000, 24000, true),
                    new MusicSound(net.minecraft.sound.SoundEvents.MUSIC_MENU, 12000, 24000, true),
                    new MusicSound(net.minecraft.sound.SoundEvents.MUSIC_DRAGON, 12000, 24000, true),
                    new MusicSound(net.minecraft.sound.SoundEvents.MUSIC_END, 12000, 24000, true)
                };
                
                int randomIndex = (int)(Math.random() * overworldMusic.length);
                MusicSound selectedMusic = overworldMusic[randomIndex];
                boolean shouldPlay = type.equals(selectedMusic);
                
                if (shouldPlay) {
                    lastRandomMusic = selectedMusic;
                }
                
                cir.setReturnValue(shouldPlay);
            }
        }
    }
}
