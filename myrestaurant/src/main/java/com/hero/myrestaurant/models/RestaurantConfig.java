package com.hero.myrestaurant.models;

import org.hibernate.validator.constraints.URL;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@JsonIgnoreProperties({"hibernateLazyInitializer"})
@Entity
@Table(name = "restaurantConfigs",  uniqueConstraints = {
    @UniqueConstraint(columnNames = "restaurantUrl")
})
public class RestaurantConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 20)
    private String restaurantName;

    @NotBlank
    @URL
    private String restaurantUrl;

    @NotBlank
    private String theme;

    @NotBlank
    private String restaurantAddress;

    @NotBlank
    @Size(max = 20)
    private String restaurantPhone;

    @NotBlank
    @Size(max = 50)
    @Email
    private String restaurantEmail;

    @OneToOne(fetch = FetchType.EAGER)
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    public RestaurantConfig() {
    }

    public RestaurantConfig(String restaurantName, String restaurantUrl, String theme, String restaurantAddress,
            String restaurantPhone, String restaurantEmail, User user) {
        this.restaurantName = restaurantName;
        this.restaurantUrl = restaurantUrl;
        this.theme = theme;
        this.restaurantAddress = restaurantAddress;
        this.restaurantPhone = restaurantPhone;
        this.restaurantEmail = restaurantEmail;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getRestaurantUrl() {
        return restaurantUrl;
    }

    public void setRestaurantUrl(String restaurantUrl) {
        this.restaurantUrl = restaurantUrl;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getRestaurantAddress() {
        return restaurantAddress;
    }

    public void setRestaurantAddress(String restaurantAddress) {
        this.restaurantAddress = restaurantAddress;
    }

    public String getRestaurantPhone() {
        return restaurantPhone;
    }

    public void setRestaurantPhone(String restaurantPhone) {
        this.restaurantPhone = restaurantPhone;
    }

    public String getRestaurantEmail() {
        return restaurantEmail;
    }

    public void setRestaurantEmail(String restaurantEmail) {
        this.restaurantEmail = restaurantEmail;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
