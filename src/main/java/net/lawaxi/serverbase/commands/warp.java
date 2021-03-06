package net.lawaxi.serverbase.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.lawaxi.serverbase.utils.WorldDiscription;
import net.lawaxi.serverbase.utils.config.configs;
import net.lawaxi.serverbase.utils.config.messages;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.LiteralText;

import java.io.*;

public class warp{
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) throws IOException
    {
        dispatcher.register(CommandManager.literal("warp")
                        .then(CommandManager.argument(messages.get(3,"null"), StringArgumentType.string())
                                .executes(ctx -> {

                                    String warpname = StringArgumentType.getString(ctx,messages.get(3,"null"));
                                    File warpfile = new File(configs.warpfolder, warpname+".yml");
                                    if(warpfile.exists())
                                    {
                                        try{
                                            FileInputStream fos = new FileInputStream(warpfile);

                                            BufferedReader buffer = new BufferedReader(new InputStreamReader(fos, "UTF-8"));
                                            String worldx = buffer.readLine();
                                            if(worldx!=null){
                                                String sx =buffer.readLine();
                                                if(sx!=null)
                                                {
                                                    String sy =buffer.readLine();
                                                    if(sy!=null)
                                                    {
                                                        String sz =buffer.readLine();
                                                        if(sz!=null)
                                                        {

                                                            ServerPlayerEntity player =ctx.getSource().getPlayer();
                                                            ServerWorld world = WorldDiscription.getWorld(worldx,player.getServer());

                                                            double x = Double.valueOf(sx);
                                                            double y = Double.valueOf(sy);
                                                            double z = Double.valueOf(sz);

                                                            //locationinfo.recordlocation(player);
                                                            player.sendMessage(new LiteralText(messages.get(1,player.getGameProfile().getName())),false);
                                                            player.sendMessage(new LiteralText(messages.get(2,player.getGameProfile().getName()).replace("%to%",warpname)),true);
                                                            player.teleport(world,x,y,z,0,0);

                                                            return 1;

                                                        }
                                                    }
                                                }
                                            }

                                        }
                                        catch (IOException e)
                                        {
                                        }
                                    }

                                    ctx.getSource().getPlayer().sendMessage(new LiteralText(messages.get(4,ctx.getSource().getPlayer().getGameProfile().getName()).replace("%to%",warpname)),false);
                                    return 1;
                                } ))
                        .executes(ctx -> {
                            warps.getWarps(ctx.getSource().getPlayer());
                            return 1;
                        })
        );
    }
}
