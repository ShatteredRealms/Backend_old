package me.wilsimpson.testmmo.models.web;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "privileges")
public class Privilege
{
    @Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Getter
    private Long id;

    @Getter
	@Setter
    private String name;

    public Privilege(String name)
    {
        this.name = name;
    }

    public Privilege() { }
}
