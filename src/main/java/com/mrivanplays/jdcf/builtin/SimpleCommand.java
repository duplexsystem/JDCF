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
package com.mrivanplays.jdcf.builtin;

import com.mrivanplays.jdcf.Command;
import com.mrivanplays.jdcf.CommandExecutionContext;
import com.mrivanplays.jdcf.args.CommandArguments;

import net.dv8tion.jda.api.Permission;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a normal command which gives you a {@link String[]} for arguments except the traditional argument handler
 * for JDCF.
 */
public abstract class SimpleCommand extends Command {

    public SimpleCommand(@NotNull String name) {
        super(name);
    }

    public SimpleCommand(@NotNull String name, @Nullable Permission... permissions) {
        super(name, permissions);
    }

    @Override
    public void execute(@NotNull CommandExecutionContext context, @NotNull CommandArguments args) {
        execute(context, args.getArgsLeft());
    }

    /**
     * Command execution logic, called when the specified command was executed
     *
     * @param context the command context
     * @param args arguments
     */
    public abstract void execute(@NotNull CommandExecutionContext context, @NotNull String[] args);
}