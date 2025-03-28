package br.com.matteusmoreno.garage_manager.employee.constant;

import lombok.Getter;

@Getter
public enum EmployeeRole {
    ADMIN("Admin"),
    SELLER("Seller"),
    MECHANIC("Mechanic"),
    MANAGER("Manager"),
    OTHER("Other");

    private final String displayName;

    EmployeeRole(String displayName) {
        this.displayName = displayName;
    }
}
