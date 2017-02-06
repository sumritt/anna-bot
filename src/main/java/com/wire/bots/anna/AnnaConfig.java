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

import javax.validation.constraints.NotNull;

public class AnnaConfig extends com.wire.bots.sdk.Configuration {
    public String name;
    public int accent;

    @NotNull
    public PandoraConfig pandora;
    public long delay = 2; // delay in mills when replying

    public PandoraConfig getPandora() {
        return pandora;
    }

    public String getName() {
        return name;
    }

    public int getAccent() {
        return accent;
    }

    public long getDelay() {
        return delay;
    }

    public static class PandoraConfig {
        String appId;
        String userKey;
        String bot;

        public String getAppId() {
            return appId;
        }

        public String getUserKey() {
            return userKey;
        }

        public String getBot() {
            return bot;
        }
    }
}
