package shukaro.artifice.event;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;
import shukaro.artifice.net.PacketDispatcher;

public class ArtificeClientTickHandler
{
    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void keyTicker(ClientTickEvent cte)
    {
        if (cte.phase != TickEvent.Phase.START) return;
        final Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer == null)
            return;
        Integer playerID = mc.thePlayer.getEntityId();
        if (Keyboard.isKeyDown(mc.gameSettings.keyBindSneak.getKeyCode()) && !Tracking.sneaks.contains(playerID))
        {
            Tracking.sneaks.add(playerID);
            PacketDispatcher.sendSneakEvent(playerID);
        }
        else if (!Keyboard.isKeyDown(mc.gameSettings.keyBindSneak.getKeyCode()) && Tracking.sneaks.contains(playerID))
        {
            Tracking.sneaks.remove(playerID);
            PacketDispatcher.sendSneakEvent(playerID);
        }
    }
}
