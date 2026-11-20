package com.cooklist.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cooklist.dto.UserAdminCreateDto;
import com.cooklist.dto.UserCreateDto;
import com.cooklist.dto.UserResponseDto;
import com.cooklist.dto.UserUpdateDto;
import com.cooklist.mapper.UserMapper;
import com.cooklist.model.Role;
import com.cooklist.model.User;
import com.cooklist.repository.RoleRepository;
import com.cooklist.repository.UserRepository;
import com.cooklist.util.DataConstant;
import com.cooklist.util.MessageConstant;
import com.cooklist.util.exception.ResourceNotFoundException;

@Service
public class UserService {

    private final static Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserService(
            UserRepository userRepository,
            RoleRepository roleRepository,
            UserMapper userMapper,
            PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public List<UserResponseDto> findAll() {

        LOGGER.info("Buscando todos los usuarios");

        List<UserResponseDto> userResponseDtoList = new ArrayList<>();

        List<User> users = userRepository.findAll();
        for (User user : users) {
            UserResponseDto userResponseDto = userMapper.toResponseDto(user);
            userResponseDtoList.add(userResponseDto);
        }

        return userResponseDtoList;
    }

    @Transactional(readOnly = true)
    public UserResponseDto findByEmail(String email) {

        LOGGER.info("Buscando el usuario con email {}", email);

        UserResponseDto userResponseDto = null;

        Optional<User> userOpt = userRepository.findByEmail(email);

        if (userOpt.isPresent()) {
            userResponseDto = userMapper.toResponseDto(userOpt.get());
        }

        return userResponseDto;
    }

    @Transactional
    public UserResponseDto createCustomer(UserCreateDto dto) throws ResourceNotFoundException {

        LOGGER.info("Creando un nuevo cliente");

        if (userRepository.existsByEmail(dto.email())) {
            throw new ResourceNotFoundException(MessageConstant.EMAIL_EXISTS);
        }
        Role role = roleRepository.findByName(DataConstant.CLIENT_ROLE)
                .orElseThrow(() -> new ResourceNotFoundException(MessageConstant.CLIENT_ROLE_DOESNT_EXIST));

        String encodedPassword = passwordEncoder.encode(dto.password());

        User user = userMapper.toEntity(dto, role, encodedPassword);
        user.setActive(Boolean.TRUE);
        userRepository.save(user);

        return userMapper.toResponseDto(user);
    }

    @Transactional
    public void updateCurrent(UserUpdateDto userUpdateDto, String email) throws ResourceNotFoundException {

        LOGGER.info("Actualizando el perfil del usuario {}", email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(MessageConstant.USER_DOESNT_EXIST));

        user.setName(userUpdateDto.name());
        user.setFamilySize(userUpdateDto.familySize());
        user.setBirthDate(userUpdateDto.birthDate());

        userRepository.save(user);

    }

    @Transactional(readOnly = true)
    public List<UserResponseDto> findByNameContaining(String name) {

        LOGGER.info("Buscando usuarios cuyo nombre contiene '{}'", name);

        List<UserResponseDto> userResponseDtoList = new ArrayList<>();

        List<User> users = userRepository.findByNameContaining(name);
        if (CollectionUtils.isNotEmpty(users)) {
            for (User user : users) {
                UserResponseDto userResponseDto = userMapper.toResponseDto(user);
                userResponseDtoList.add(userResponseDto);
            }
        }

        return userResponseDtoList;
    }

    @Transactional(readOnly = true)
    public UserResponseDto findById(Long id) throws ResourceNotFoundException {

        LOGGER.info("Buscando el usuario con id {}", id);

        Optional<User> userOpt = userRepository.findById(id);
        User user = userOpt.orElseThrow(() -> new ResourceNotFoundException(MessageConstant.USER_DOESNT_EXIST));

        return userMapper.toResponseDto(user);
    }

    @Transactional(readOnly = true)
    public UserResponseDto findCurrentUser(String email) throws ResourceNotFoundException {

        LOGGER.info("Buscando el usuario logueado {}", email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(MessageConstant.USER_DOESNT_EXIST));

        return userMapper.toResponseDto(user);
    }

    @Transactional
    public UserResponseDto createByAdmin(UserAdminCreateDto dto) throws ResourceNotFoundException {

        LOGGER.info("Creando usuario desde administrador");

        if (userRepository.existsByEmail(dto.email())) {
            throw new ResourceNotFoundException(MessageConstant.EMAIL_EXISTS);
        }
        Role role = roleRepository.findById(dto.roleId())
                .orElseThrow(() -> new ResourceNotFoundException(MessageConstant.ROLE_DOESNT_EXIST));

        String encodedPassword = passwordEncoder.encode(dto.password());

        User user = userMapper.toEntity(dto, role, encodedPassword);
        user.setActive(Boolean.TRUE);
        userRepository.save(user);

        return userMapper.toResponseDto(user);
    }

    @Transactional
    public void update(Long id, UserUpdateDto userUpdateDto) throws ResourceNotFoundException {

        LOGGER.info("Actualizando el usuario con id {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(MessageConstant.USER_DOESNT_EXIST));

        user.setName(userUpdateDto.name());
        user.setFamilySize(userUpdateDto.familySize());
        user.setBirthDate(userUpdateDto.birthDate());

        userRepository.save(user);
    }

    @Transactional
    public void deactivate(Long id) throws ResourceNotFoundException {

        LOGGER.info("Desactivando el usuario con id {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(MessageConstant.USER_DOESNT_EXIST));

        user.setActive(Boolean.FALSE);

        userRepository.save(user);
    }
}