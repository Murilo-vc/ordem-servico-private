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

    public static UserListPayload parseUserListPayload(final String jsonString) {
        final Gson gson = new Gson();
        return gson.fromJson(jsonString, UserListPayload.class);
    }

    public static WorkOrderCreatePayload parseCreateWorkOrderPayload(final String jsonString) {
        return new Gson().fromJson(jsonString, WorkOrderCreatePayload.class);
    }

    public static WorkOrderListPayload parseWorkOrderListPayload(final String jsonString) {
        return new Gson().fromJson(jsonString, WorkOrderListPayload.class);
    }

    public static WorkOrderUpdatePayload parseUpdateWorkOrderPayload(final String jsonString) {
        return new Gson().fromJson(jsonString, WorkOrderUpdatePayload.class);
    }

    public static AlterWorkOrderPayload parseChangeWorkOrderStatusPayload(final String jsonString) {
        return new Gson().fromJson(jsonString, AlterWorkOrderPayload.class);
    }

    public static String getErrorJson(final ErrorDto error) {
        return new Gson().toJson(error);
    }
}
