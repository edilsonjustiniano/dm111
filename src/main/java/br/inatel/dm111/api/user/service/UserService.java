package br.inatel.dm111.api.user.service;

import br.inatel.dm111.api.core.ApiException;
import br.inatel.dm111.api.core.AppErrorCode;
import br.inatel.dm111.api.user.UserResponse;
import br.inatel.dm111.api.user.controller.UserRequest;
import br.inatel.dm111.persistence.user.User;
import br.inatel.dm111.persistence.user.UserFirebaseRepository;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Service
public class UserService {

    private final UserFirebaseRepository repository;

    public UserService(UserFirebaseRepository repository) {
        this.repository = repository;
    }

    public List<UserResponse> searchUsers() throws ApiException {
        try {
            return repository.findAll().stream()
                    .map(this::toUserResponse)
                    .toList();
        } catch (ExecutionException | InterruptedException e) {
            throw new ApiException(AppErrorCode.USERS_QUERY_ERROR);
        }
    }


    public UserResponse createUser(UserRequest request) throws ApiException {
        validateUser(request);
        var user = buildUser(request);
        repository.save(user);

        return toUserResponse(user);
    }

    private void validateUser(UserRequest request) throws ApiException {
        try {
            var conflictedUserOpt = repository.findByEmail(request.email());
            if (conflictedUserOpt.isPresent()) {
                throw new ApiException(AppErrorCode.USER_CONFLICT_EMAIL);
            }
        } catch (ExecutionException | InterruptedException e) {
            throw new ApiException(AppErrorCode.USERS_QUERY_ERROR);
        }
    }

    private UserResponse toUserResponse(User user) {
        return new UserResponse(user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole());
    }

    private String encryptPassword(String password) throws ApiException {
        MessageDigest crypt = null;
        try {
            crypt = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            throw new ApiException(AppErrorCode.PASSWORD_ENCRYPTION_ERROR);
        }
        crypt.reset();
        crypt.update(password.getBytes(StandardCharsets.UTF_8));

        return new BigInteger(1, crypt.digest()).toString();
    }

    private User buildUser(UserRequest request) throws ApiException {
        var id = UUID.randomUUID().toString();
        return new User(id,
                request.name(),
                request.email(),
                encryptPassword(request.password()),
                request.role());
    }

}
