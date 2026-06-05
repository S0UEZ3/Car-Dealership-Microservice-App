package org.example.dataAccess.models;

public enum Permission {
    // Car
    VIEW_CARS_IN_STOCK,
    VIEW_CAR_DETAILS,
    FILTER_CARS,

    // Orders
    VIEW_ORDERS_IN_STOCK,
    VIEW_ORDERS_CUSTOM,
    CREATE_ORDER_IN_STOCK,
    CREATE_ORDER_CUSTOM,
    UPDATE_ORDER_STATUS,

    // Test drives
    VIEW_TEST_DRIVE_REQUESTS,
    CREATE_TEST_DRIVE_REQUEST,
    MANAGE_TEST_DRIVE_CARS,

    // Stock administration
    ADD_CAR,
    UPDATE_CAR,
    DELETE_CAR,
    ADD_COMPONENT,
    UPDATE_COMPONENT,
    DELETE_COMPONENT,
    VIEW_COMPONENT,
    VIEW_COMPONENTS_LIST,

    // System administration
    MANAGE_USERS,
    MANAGE_ALL_ENTITIES
}
