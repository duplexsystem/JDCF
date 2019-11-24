/*
    Copyright (c) 2019 Ivan Pekov
    Copyright (c) 2019 Contributors

    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:

    The above copyright notice and this permission notice shall be included in all
    copies or substantial portions of the Software.

    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
    SOFTWARE.
*/
package com.mrivanplays.jdcf.settings.prefix;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mrivanplays.jdcf.util.Utils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class DefaultPrefixHandler implements PrefixHandler {

    private final Map<Long, String> prefixes = new HashMap<>();
    private final File file;
    private ObjectMapper jsonMapper;
    private String defaultPrefix;

    public DefaultPrefixHandler(ScheduledExecutorService executorService) {
        this(executorService, new ObjectMapper());
    }

    public DefaultPrefixHandler(ScheduledExecutorService executorService, ObjectMapper jsonMapper) {
        TypeReference<HashMap<Long, String>> mapType = new TypeReference<HashMap<Long, String>>() {
        };
        this.jsonMapper = jsonMapper;
        file = new File("prefixes.json");
        createFile();
        executorService.scheduleAtFixedRate(this::savePrefixes, 5, 30, TimeUnit.MINUTES);
        try (Reader reader = new FileReader(file)) {
            Map<Long, String> map = jsonMapper.readValue(reader, mapType);
            if (map == null) {
                return;
            }
            prefixes.putAll(map);
        } catch (IOException ignored) {
        }
    }

    private void createFile() {
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException ignored) {
            }
        }
    }

    @Override
    public @NotNull String getDefaultPrefix() {
        if (defaultPrefix == null) {
            setDefaultPrefix("!");
        }
        return defaultPrefix;
    }

    @Override
    public void setDefaultPrefix(@NotNull String defaultPrefix) {
        this.defaultPrefix = Objects.requireNonNull(defaultPrefix, "defaultPrefix");
    }

    @Override
    public @Nullable String getGuildPrefix(long guildId) {
        return prefixes.get(guildId);
    }

    @Override
    public void setGuildPrefix(@NotNull String prefix, long guildId) {
        Objects.requireNonNull(prefix, "prefix");
        Utils.checkState(!(guildId <= 0), "Guild id cannot be 0 or less than 0");
        if (!prefixes.containsKey(guildId)) {
            prefixes.put(guildId, prefix);
        } else {
            prefixes.replace(guildId, prefix);
        }
    }

    @Override
    public void savePrefixes() {
        file.delete();
        createFile();
        try (Writer writer = new FileWriter(file)) {
            writer.write(jsonMapper.writer().writeValueAsString(prefixes));
        } catch (IOException ignored) {
        }
    }
}
