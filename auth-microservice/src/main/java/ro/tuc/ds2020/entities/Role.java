package ro.tuc.ds2020.entities;

/**
 * Enum representing user roles in the system.
 * ADMIN has full CRUD access; CLIENT has read-only access to own devices.
 */
public enum Role {
    /** Full administrative privileges. */
    ADMIN,
    /** Limited access to own assigned devices. */
    CLIENT
}
