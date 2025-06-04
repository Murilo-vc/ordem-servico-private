package org.example.murilo.ordemservico.util;

import com.google.gson.Gson;
import org.example.murilo.ordemservico.domain.dto.ErrorDto;
import org.example.murilo.ordemservico.domain.payload.*;

public final class JsonUtils {

    public static OperationPayload parseOperationPayload(final String jsonString) {
        final Gson gson = new Gson();
        return gson.fromJson(jsonString, OperationPayload.class);
    }

    public static LoginPayload parseLoginPayload(final String jsonString) {
        final Gson gson = new Gson();
        return gson.fromJson(jsonString, LoginPayload.class);
    }

    public static LogoutPayload parseLogoutPayload(final String jsonString) {
        final Gson gson = new Gson();
        return gson.fromJson(jsonString, LogoutPayload.class);
    }

    public static CheckProfilePayload parseCheckProfilePayload(final String jsonString) {
        final Gson gson = new Gson();
        return gson.fromJson(jsonString, CheckProfilePayload.class);
    }

    public static SignUpPayload parseSignUpPayload(final String jsonString) {
        final Gson gson = new Gson();
        return gson.fromJson(jsonString, SignUpPayload.class);
    }

    public static UpdateUserPayload parseUpdateUserPayload(final String jsonString) {
        final Gson gson = new Gson();
        return gson.fromJson(jsonString, UpdateUserPayload.class);
    }

    public static DeleteUserPayload parseDeleteUserPayload(final String jsonString) {
        final Gson gson = new Gson();
        return gson.fromJson(jsonString, DeleteUserPayload.class);
    }

    public static String getErrorJson(final ErrorDto error) {
        return new Gson().toJson(error);
    }
}
