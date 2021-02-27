package com.aliucord;

import android.content.Context;
import android.content.Intent;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.aliucord.fragments.AppFragmentProxy;
import com.discord.api.commands.CommandChoice;
import com.discord.api.user.User;
import com.discord.app.AppComponent;
import com.discord.models.domain.*;
import com.discord.stores.StoreMessages;
import com.discord.stores.StoreStream;
import com.discord.utilities.SnowflakeUtils;
import com.discord.utilities.fcm.NotificationClient;
import com.discord.utilities.time.ClockFactory;
import com.discord.utilities.time.TimeUtils;
import com.discord.utilities.user.UserUtils;
import com.discord.views.CheckedSetting;
import com.google.gson.Gson;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import kotlin.jvm.functions.Function1;

@SuppressWarnings("unused")
public class Utils {
    public static Context appContext;

    public static void setAppContext() {
        appContext = NotificationClient.access$getContext$p(NotificationClient.INSTANCE);
    }
    public static Context getAppContext() {
        if (appContext == null) setAppContext();
        return appContext;
    }

    public static int getResId(String name, String type) {
        Context context = getAppContext();
        if (context == null) return 0;
        return context.getResources().getIdentifier(name, type, "com.discord");
    }

    private static float density;
    public static int dpToPx(int dp) { return Utils.dpToPx((float) dp); }
    public static int dpToPx(float dp) {
        if (density == 0) density = Utils.getAppContext().getResources().getDisplayMetrics().density;
        return (int) (dp * Utils.getAppContext().getResources().getDisplayMetrics().density + 0.5f);
    }

    private static int defaultPadding = 0;
    public static int getDefaultPadding() {
        if (defaultPadding == 0) defaultPadding = Utils.dpToPx(16);
        return defaultPadding;
    }

    public static <K, V> K getMapKey(Map<K, V> map, V val) {
        for (Map.Entry<K, V> entry : map.entrySet()) {
            if (Objects.equals(val, entry.getValue())) return entry.getKey();
        }
        return null;
    }

    public static void openPage(Context context, Class<? extends AppComponent> clazz) { Utils.openPage(context, clazz, null); }
    public static void openPage(Context context, Class<? extends AppComponent> clazz, Intent intent) { f.a.b.m.c(context, clazz, intent); }
    public static void openPageWithProxy(Context context, Fragment fragment) {
        String id = String.valueOf(SnowflakeUtils.fromTimestamp(System.currentTimeMillis() * 100));
        AppFragmentProxy.fragments.put(id, fragment);
        Utils.openPage(context, AppFragmentProxy.class, new Intent().putExtra("AC_FRAGMENT_ID", id));
    }

    public static CommandChoice createCommandChoice(String name, String value) {
        CommandChoice choice = new CommandChoice();
        try {
            setPrivateField(CommandChoice.class, choice, "name", name);
            setPrivateField(CommandChoice.class, choice, "value", value);
        } catch (Throwable e) { Main.logger.error(e); }
        return choice;
    }

    public static User CLYDE = new User(
            -1,
            "Clyde",
            null,
            "0000",
            0,
            null,
            true,
            false,
            null,
            null,
            false,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null
    );

    public static void createClydeMessage(String content, long channelId, List<ModelMessageEmbed> embeds) {
        long nonce = NonceGenerator.computeNonce();
        ModelMessage message = new ModelMessage(
                nonce,
                String.valueOf(nonce),
                channelId,
                ModelMessage.TYPE_LOCAL,
                content,
                CLYDE,
                null,
                TimeUtils.currentTimeUTCDateString(ClockFactory.get()),
                null,
                null,
                embeds,
                false,
                null,
                false,
                null,
                null,
                0L,
                null,
                null,
                false,
                null,
                false,
                null,
                null,
                null,
                null,
                null,
                null,
                0,
                null
        );
        StoreMessages.access$handleLocalMessageCreate(StoreStream.getMessages(), message);
    }

    public static Object getPrivateField(Class<?> clazz, Object instance, String fieldName) throws Exception {
        Field field = clazz.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(instance);
    }

    public static void setPrivateField(Class<?> clazz, Object instance, String fieldName, Object v) throws Exception {
        Field field = clazz.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(instance, v);
    }

    // added in api 24, minimum is 21 so needed to reimplement
    @SuppressWarnings("UnusedReturnValue")
    public static <E> boolean removeIf(Collection<E> collection, Function1<? super E, Boolean> filter) {
        Objects.requireNonNull(filter);
        boolean removed = false;
        final Iterator<E> each = collection.iterator();
        while (each.hasNext()) {
            if (filter.invoke(each.next())) {
                each.remove();
                removed = true;
            }
        }
        return removed;
    }

    public static CheckedSetting createCheckedSetting(Context context, CheckedSetting.ViewType type, CharSequence text, CharSequence subtext) {
        CheckedSetting cs = new CheckedSetting(context, null);
        if (!type.equals(CheckedSetting.ViewType.CHECK)) {
            cs.removeAllViews();
            cs.f(type);
        }

        TextView textView = cs.g.a();
        textView.setTextSize(16.0f);
        textView.setTypeface(ResourcesCompat.getFont(context, Constants.Fonts.whitney_medium));
        textView.setText(text);
        cs.setSubtext(subtext);
        return cs;
    }

    public static Gson gson = new Gson();
    public static <T> T fromJson(String json, Type type) { return gson.g(json, type); }
    public static String toJson(Object obj) { return gson.l(obj); }

    public static CharSequence renderMD(CharSequence source) {
        return p.a.b.b.a.P(source, null, 1);
    }

    public static void log(String msg) { Main.logger.debug(msg); }
}