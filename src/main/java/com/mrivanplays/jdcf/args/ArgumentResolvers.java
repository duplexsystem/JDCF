package com.mrivanplays.jdcf.args;

import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A utility class containing the default argument resolvers.
 */
public final class ArgumentResolvers {

    public static ArgumentResolver<Integer> INTEGER = context -> Integer.parseInt(context.getArgument());
    public static ArgumentResolver<Double> DOUBLE = context -> Double.parseDouble(context.getArgument());
    public static ArgumentResolver<Float> FLOAT = context -> Float.parseFloat(context.getArgument());

    public static ArgumentResolver<User> USER_MENTION = new ArgumentResolver<User>() {

        private final Pattern UM_PATTERN = Pattern.compile("<@!?(\\d{17,20})>");

        @Override
        public User resolve(@NotNull ArgumentResolverContext context) throws Exception {
            Matcher matcher = UM_PATTERN.matcher(context.getArgument());
            if (matcher.matches()) {
                User user = context.getJda().getUserById(matcher.group(1));
                if (user == null) {
                    // represent that the value present wasn't a mention
                    throw new IllegalArgumentException();
                } else {
                    return user;
                }
            } else {
                // represent that the value wasn't a mention
                throw new IllegalArgumentException();
            }
        }
    };

    public static ArgumentResolver<User> USER_ID = context -> {
        User user = context.getJda().getUserById(Long.parseLong(context.getArgument()));
        if (user == null) {
            throw new IllegalArgumentException();
        } else {
            return user;
        }
    };

    public static ArgumentResolver<User> USER = context -> {
        try {
            return USER_ID.resolve(context);
        } catch (Exception e) {
            return USER_MENTION.resolve(context);
        }
    };

    public static ArgumentResolver<Role> ROLE_NAME = context -> {
        List<Role> roles = context.getGuild().getRolesByName(context.getArgument(), true);
        if (roles.isEmpty()) {
            throw new IllegalArgumentException();
        } else {
            return roles.get(0);
        }
    };

    public static ArgumentResolver<Role> ROLE_ID = context -> {
        Role role = context.getGuild().getRoleById(context.getArgument());
        if (role == null) {
            throw new IllegalArgumentException();
        } else {
            return role;
        }
    };

    public static ArgumentResolver<Role> ROLE = context -> {
        try {
            return ROLE_NAME.resolve(context);
        } catch (Exception e) {
            return ROLE_ID.resolve(context);
        }
    };

    public static ArgumentResolver<TextChannel> CHANNEL_NAME = context -> {
        List<TextChannel> channels = context.getGuild().getTextChannelsByName(context.getArgument(), true);

        if (channels.isEmpty()) {
            throw new IllegalArgumentException();
        } else {
            return channels.get(0);
        }
    };

    public static ArgumentResolver<TextChannel> CHANNEL_ID = context -> {
        TextChannel channel = context.getGuild().getTextChannelById(context.getArgument());
        if (channel == null) {
            throw new IllegalArgumentException();
        } else {
            return channel;
        }
    };

    public static ArgumentResolver<TextChannel> CHANNEL = context -> {
        try {
            return CHANNEL_NAME.resolve(context);
        } catch (Exception e) {
            return CHANNEL_ID.resolve(context);
        }
    };
}
