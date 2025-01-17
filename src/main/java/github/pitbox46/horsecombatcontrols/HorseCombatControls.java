package github.pitbox46.horsecombatcontrols;

import github.pitbox46.horsecombatcontrols.network.ClientProxy;
import github.pitbox46.horsecombatcontrols.network.CombatModePacket;
import github.pitbox46.horsecombatcontrols.network.CommonProxy;
import github.pitbox46.horsecombatcontrols.network.PacketHandler;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.network.PacketDistributor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("horsecombatcontrols")
public class HorseCombatControls {
    public static final Logger LOGGER = LogManager.getLogger();

    public static CommonProxy PROXY;

    public HorseCombatControls() {
        PROXY = DistExecutor.safeRunForDist(() -> ClientProxy::new, () -> CommonProxy::new);
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, Config.SERVER_CONFIG);
        MinecraftForge.EVENT_BUS.register(this);
    }

    public static boolean isInCombatMode(Player player) {
        return ((CombatModeAccessor) player).inCombatMode();
    }

    @SubscribeEvent
    public void onPlayerLog(PlayerEvent.PlayerLoggedInEvent event) {
        if(!(event.getEntity() instanceof ServerPlayer player)) return;
        CommonProxy.setCombatModeServerVersion(player, isInCombatMode(player));
    }
}
