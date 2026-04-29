package com.hotel.auth;

/**
 * Represents a guest in the system.
 */
public class Guest {
    private int guestId;
    private int userId;
    private String name;
    private String contactInfo;

    /**
     * Constructs a new Guest.
     * @param guestId the guest ID
     * @param userId the linked user ID
     * @param name the guest's name
     * @param contactInfo the contact information
     */
    public Guest(int guestId, int userId, String name, String contactInfo) {
        this.guestId = guestId;
        this.userId = userId;
        this.name = name;
        this.contactInfo = contactInfo;
    }

    /**
     * Constructs a new Guest before persistence.
     * @param userId the linked user ID
     * @param name the guest's name
     * @param contactInfo the contact information
     */
    public Guest(int userId, String name, String contactInfo) {
        this.userId = userId;
        this.name = name;
        this.contactInfo = contactInfo;
    }

    /**
     * Gets the guest ID.
     * @return the guest ID
     */
    public int getGuestId() {
        return guestId;
    }

    /**
     * Sets the guest ID.
     * @param guestId the guest ID
     */
    public void setGuestId(int guestId) {
        this.guestId = guestId;
    }

    /**
     * Gets the linked user ID.
     * @return the user ID
     */
    public int getUserId() {
        return userId;
    }

    /**
     * Sets the linked user ID.
     * @param userId the user ID
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     * Gets the guest's name.
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the guest's name.
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the contact information.
     * @return the contact info
     */
    public String getContactInfo() {
        return contactInfo;
    }

    /**
     * Sets the contact information.
     * @param contactInfo the contact info
     */
    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    @Override
    public String toString() {
        return "Guest{id=" + guestId + ", userId=" + userId + ", name='" + name + "', contact='" + contactInfo + "'}";
    }
}