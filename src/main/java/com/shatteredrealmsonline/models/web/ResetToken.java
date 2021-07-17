package com.shatteredrealmsonline.models.web;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@ToString(exclude = "user")
@EqualsAndHashCode(exclude = "user")
@Entity
@Table(name = "reset_tokens")
public class ResetToken
{
    // Expiration is valid for 15 minutes
    private static final int EXPIRATION = 15 * 1000 * 60;

    @Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Getter
    private int id;

	@Getter
    private String token;

    @Getter
    @ManyToOne
    private User user;

    @Getter
    @Setter
    private Date generationDate;

    public ResetToken() { }

    public ResetToken(User user)
    {
        this.user = user;
        this.token = UUID.randomUUID().toString();
        this.generationDate = new Date(System.currentTimeMillis());
    }

    public void refreshToken()
    {
        generationDate = new Date(System.currentTimeMillis());
    }

    public boolean hasTokenExpired()
    {
        return generationDate.toInstant().toEpochMilli()-System.currentTimeMillis() > EXPIRATION;
    }
}
