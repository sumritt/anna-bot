//
// Wire
// Copyright (C) 2016 Wire Swiss GmbH
//
// This program is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program. If not, see http://www.gnu.org/licenses/.
//

package com.wire.bots.anna;

import com.wire.bots.sdk.Logger;
import com.wire.bots.sdk.MessageHandlerBase;
import com.wire.bots.sdk.WireClient;
import com.wire.bots.sdk.models.ImageMessage;
import com.wire.bots.sdk.models.TextMessage;
import com.wire.bots.sdk.server.model.NewBot;
import com.wire.bots.sdk.server.model.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class MessageHandler extends MessageHandlerBase {
    private static final int MAX_INPUT_SIZE = 128;

    private final AnnaConfig config;
    private final Pandora pandora;
    private final Timer timer = new Timer("MessageTimer");

    public MessageHandler(AnnaConfig config) {
        this.config = config;
        pandora = new Pandora(config);
    }

    @Override
    public void onNewConversation(final WireClient client) {
        Logger.info(String.format("onNewConversation: bot: %s, conv: %s",
                client.getId(),
                client.getConversationId()));

        final String text = "Hi, I'm Anna, an experimental chat bot. " +
                "You can ask me things, and even teach me. What shall we talk about?";

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    client.sendText(text);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, TimeUnit.SECONDS.toMillis(2));
    }

    @Override
    public void onText(WireClient client, TextMessage msg) {
        try {
            Logger.info(String.format("Received Text. bot: %s, from: %s", client.getId(), msg.getUserId()));

            reply(client, msg.getText());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onImage(WireClient client, ImageMessage msg) {
        try {
            Logger.info(String.format("Received Image: type: %s, size: %,d KB, h: %d, w: %d, tag: %s",
                    msg.getMimeType(),
                    msg.getSize() / 1024,
                    msg.getHeight(),
                    msg.getWidth(),
                    msg.getTag()
            ));

            reply(client, "ANNAIMAGE");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMemberJoin(WireClient client, ArrayList<String> userIds) {
        try {
            Collection<User> users = client.getUsers(userIds);
            for (User user : users) {
                Logger.info(String.format("onMemberJoin: bot: %s, user: %s/%s",
                        client.getId(),
                        user.id,
                        user.name
                ));

                // say Hi to new participant
                client.sendText("Hi there, " + user.name);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Logger.error(e.getMessage());
        }
    }

    @Override
    public void onMemberLeave(WireClient client, ArrayList<String> userIds) {
        Logger.info(String.format("onMemberLeave: users: %s, bot: %s",
                userIds,
                client.getId()));
    }

    @Override
    public void onBotRemoved(String botId) {
        Logger.info("This bot got removed from the conversation :(. BotId: " + botId);
    }

    @Override
    public boolean onNewBot(NewBot newBot) {
        Logger.info(String.format("onNewBot: bot: %s, origin: %s",
                newBot.id,
                newBot.origin.id));

        return true;
    }

    private void reply(final WireClient client, String text) throws Exception {
        final String input = text.length() > MAX_INPUT_SIZE ? text.substring(0, MAX_INPUT_SIZE) : text;

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    for (String talk : pandora.talk(client.getId(), input)) {
                        client.sendText(talk);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Logger.warning(e.getMessage());
                }
            }
        }, TimeUnit.SECONDS.toMillis(config.getDelay()));
    }
}
