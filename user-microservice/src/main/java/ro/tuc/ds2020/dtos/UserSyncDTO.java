package ro.tuc.ds2020.dtos;

import java.util.Objects;
import java.util.UUID;

public class UserSyncDTO {

    private UUID id;

    public UserSyncDTO() {
    }

    public UserSyncDTO(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserSyncDTO that = (UserSyncDTO) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
