package screret.gamerulelock;

import com.mojang.brigadier.Command;
import net.minecraft.command.impl.DifficultyCommand;
import net.minecraft.command.impl.GameRuleCommand;
import net.minecraft.tileentity.CommandBlockLogic;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.stream.Collectors;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("gamerulelock")
public class GameruleLock {

    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();

    public GameruleLock() {
        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLLoadCompleteEvent event) {
        // some preinit code

    }

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class ObjectRegistryHandler {
        @SubscribeEvent
        public static void CommandEvent(CommandEvent event) {
            Command command = event.getParseResults().getContext().getCommand();
            if (command instanceof DifficultyCommand || command instanceof GameRuleCommand) {
                try {
                    if (event.getParseResults().getContext().getSource().source instanceof CommandBlockLogic) {
                        event.setCanceled(true);
                    }
                } catch (Exception E) {
                    LOGGER.info("Caught exception " + E);
                }
            }
        }
    }
}
