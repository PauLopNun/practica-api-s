# Endpoint PATCH /{id}/name — Update de nombre en 1 query

## El problema con el approach clásico

El endpoint `PUT /api/{id}` que ya existía para actualizar un usuario completo hace **2 queries a base de datos**:

```
1. SELECT * FROM users WHERE id = ?   ← getUserById (findById)
2. UPDATE users SET name=?, age=? WHERE id = ?  ← save(existingUser)
```

Esto ocurre porque Spring Data JPA necesita primero traer la entidad al contexto de persistencia, modificarla en memoria, y después persistir los cambios.

---

## La solución: @Modifying + JPQL directo

En lugar de pasar por el ciclo fetch → modify → save, se ejecuta directamente un `UPDATE` en la base de datos con una sola query usando `@Modifying` y `@Query`.

### Repository

```java
@Modifying
@Transactional
@Query("UPDATE User u SET u.name = :name WHERE u.id = :id")
int updateNameById(@Param("id") Long id, @Param("name") String name);
```

- `@Query` permite escribir JPQL personalizado en vez de usar los métodos derivados de Spring Data.
- `@Modifying` le indica a Spring que esta query **modifica datos** (INSERT/UPDATE/DELETE), no solo los lee.
- `@Transactional` es obligatorio para operaciones de escritura con `@Modifying`.
- Devuelve `int`: el número de filas afectadas. Si es `0`, el usuario no existía.

### Service

```java
public void updateUserName(Long id, String name) {
    int updated = userRepository.updateNameById(id, name);
    if (updated == 0) throw new UserNotFoundException("No existe el usuario con id: " + id);
}
```

El retorno `int` reemplaza la necesidad de hacer un `findById` previo para verificar existencia. **Una sola ida a la base de datos hace todo**: actualiza si existe, y nos avisa con `0` si no.

### Controller

```java
@PatchMapping("/{id}/name")
public ResponseEntity<Void> updateUserName(@PathVariable Long id, @RequestParam String name) {
    userService.updateUserName(id, name);
    return ResponseEntity.noContent().build();
}
```

Se usa `PATCH` (semánticamente correcto para actualizaciones parciales) y devuelve `204 No Content` ya que no hay nada que retornar.

---

## Comparativa

| Approach | Queries a BD | Descripción |
|---|---|---|
| `PUT /{id}` (existente) | **2** | SELECT para traer entidad + UPDATE para guardar |
| `PATCH /{id}/name` (nuevo) | **1** | UPDATE directo con JPQL, sin SELECT previo |

---

## Cómo probarlo en Postman

```
PATCH http://localhost:8080/api/{id}/name?name=NuevoNombre
```

- **200 / 204** → usuario actualizado correctamente
- **404** → no existe ningún usuario con ese id
