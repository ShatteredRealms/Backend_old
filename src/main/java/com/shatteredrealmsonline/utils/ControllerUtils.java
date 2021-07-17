package com.shatteredrealmsonline.utils;

import com.shatteredrealmsonline.http.response.MessageResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.beans.PropertyDescriptor;
import java.util.*;

/**
 * Helpers for common controller actions
 */
public class ControllerUtils
{
    /**
     * Tries generically to delete an entity from the DB and persistence.
     *
     * Note: This could be done simply by calling repo#deleteById and only respond that an attempt to delete was made,
     * however this would not tell you if there was actually anything to delete.
     *
     * @param repo persistence repository to check
     * @param id id of entity
     * @param entitySimpleName entity type name, which is entity classes simple name
     * @param <T> entity type
     * @param <ID> entity id type
     * @return response of whether the deletion was successful or the entity could not be found
     */
    public static <T, ID> ResponseEntity<MessageResponse> genericDelete(JpaRepository<T, ID> repo, ID id, String entitySimpleName)
    {
        // Cant delete without and ID
        if (id == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("Error: No ID given."));

        Optional<T> oEntity = repo.findById(id);

        // If no entity with the given ID exists then we cannot delete it
        if (oEntity.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse(
                            String.format("Error: Could not find %s with ID: %s", entitySimpleName, id)
                    ));

        // Delete the entity
        repo.delete(oEntity.get());
        return  ResponseEntity.ok(new MessageResponse(String.format("%s deleted.", entitySimpleName)));
    }

    /**
     * Tries generically to patch an entity from the DB and persistence.
     *
     * @param repo persistence repository to check
     * @param newEntity new entity properties
     * @param id id of entity
     * @param <T> entity type
     * @param <ID> entity id type
     * @return response of whether the patch was successful or the entity could not be found
     */
    public static <T, ID> ResponseEntity<MessageResponse> genericPatch(JpaRepository<T, ID> repo, T newEntity, ID id)
    {
        // Require ID to update
        if (id == null)
            return ResponseEntity.badRequest().body(new MessageResponse("Error: No ID given."));

        Optional<T> oOriginalEntity = repo.findById(id);

        // Require existing to update
        if (oOriginalEntity.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new MessageResponse(
                            String.format("Error: Could not find %s with ID: %s", newEntity.getClass().getSimpleName(), id)
                    ));

        try
        {
            // Copy all non-null properties
            BeanWrapper beanWrapper = new BeanWrapperImpl(newEntity);
            BeanUtils.copyProperties(
                    newEntity,
                    oOriginalEntity.get(),
                    Arrays.stream(beanWrapper.getPropertyDescriptors())                       // Get all properties
                            .filter(pd -> beanWrapper.getPropertyValue(pd.getName()) == null) // Only keep null properties
                            .map(PropertyDescriptor::getName)                                 // Get the name of the property
                            .toArray(String[]::new)                                           // Create an array
            );

            // Save new entity and respond to request with success
            repo.saveAndFlush(oOriginalEntity.get());
            return  ResponseEntity.ok(new MessageResponse(String.format("%s updated.", newEntity.getClass().getSimpleName())));
        }
        catch (DataIntegrityViolationException ex)
        {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: "+ex.getMostSpecificCause().getLocalizedMessage()));
        }

    }

    /**
     * Tries generically to create a new entity in persistence and the database if there is no conflict.
     *
     * @param repo persistent repository that manages the entity
     * @param newEntity new entity to create
     * @param conflictExample conflict that would cause the creation to not be possible
     * @param conflictName variable name for the conflict
     * @param <T> entity type
     * @param <ID> entity id type
     * @return response of whether the creation was successful or a similar entity already exists
     */
    public static <T, ID> ResponseEntity<MessageResponse> genericCreate(JpaRepository<T, ID> repo, T newEntity, Example<T> conflictExample, String conflictName)
    {
        // Check for conflict
        if (repo.exists(conflictExample))
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new MessageResponse(
                            String.format("Error: %s with given %s already exists.", newEntity.getClass().getSimpleName(), conflictName)
                    ));

        // No conflict exists, create the new entity
        repo.saveAndFlush(newEntity);
        return ResponseEntity.ok(new MessageResponse(String.format("New %s created.", newEntity.getClass().getSimpleName())));
    }

    /**
     * Gets all entities in a repository or attempts to find an entity from the given id if it is non-null.
     *
     * @param repo repository to look for entity
     * @param id entity id to search for if desired
     * @param <T> entity type
     * @param <ID> entity id type
     * @return all entities found if no id was supplied, otherwise an entity with that id if it exists
     */
    public static <T, ID> ResponseEntity<Iterable<T>> genericGet(JpaRepository<T, ID> repo, ID id)
    {
        if (id == null)
            return ResponseEntity.ok(repo.findAll());

        T entity = repo.findById(id).orElse(null);
        return ResponseEntity.ok(entity == null ? List.of() : List.of(entity));
    }
}
