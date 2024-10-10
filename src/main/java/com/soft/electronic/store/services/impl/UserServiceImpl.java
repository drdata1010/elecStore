package com.soft.electronic.store.services.impl;

import com.soft.electronic.store.dtos.PageableResponse;
import com.soft.electronic.store.dtos.UserDTO;
import com.soft.electronic.store.entities.User;
import com.soft.electronic.store.exceptions.ResourceNotFound;
import com.soft.electronic.store.helper.Helper;
import com.soft.electronic.store.repositories.UserRepository;
import com.soft.electronic.store.services.UserService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper mapper;

    @Value("${user.profile.image.path}")
    private String imagePath;

    private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    @Override
    public UserDTO createUser(UserDTO userDTO) {

        //generate unique UserID in string format
        String userId = UUID.randomUUID().toString();
        userDTO.setUserId(userId);

//      converting DTO -> Entity as we cant save dto to DB
        User user = dtoToEntity(userDTO);
        User savedUser = userRepository.save(user);
//      again converting entity to DTO to entity here, as we cant return Entity
        UserDTO newDTO = entityToDTO(savedUser);
        return newDTO;
    }

    @Override
    public UserDTO updateUser(UserDTO userDTO, String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFound("User not found with given User ID!!"));
        user.setName(userDTO.getName());
        //email update
        user.setAbout(userDTO.getAbout());
        user.setGender(userDTO.getGender());
        user.setImageName(userDTO.getImageName());
        user.setPassword(userDTO.getPassword());
        //save data
        User updatedUser = userRepository.save(user);

        UserDTO updatedDto = entityToDTO(updatedUser);

        return updatedDto;
    }

    @Override
    public void deleteUser(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFound("User not found with given User ID!!"));
        String fullPath = imagePath+user.getImageName();

        try{
            Path path = Paths.get(fullPath);
            Files.delete(path);
            logger.info("Deleted image successfully : ",fullPath);
        }catch (NoSuchFileException ex){
            logger.info("User Image not Found in folder: {}",fullPath);
            ex.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //delete user by entity
        userRepository.delete(user);
    }

    @Override
    public PageableResponse<UserDTO> getAllUser(int pageNumber, int pageSize, String sortBy, String sortDir) {
        //Sort class implementation
        Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
        //page number default starts from zero
        Pageable pageable = PageRequest.of(pageNumber-1,pageSize,sort);
        Page<User> page = userRepository.findAll(pageable);
        PageableResponse<UserDTO> response = Helper.getPageableResponse(page, UserDTO.class);
        return response;
    }

    @Override
    public UserDTO getUserById(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFound("User not found with this User ID!!"));
        UserDTO foundUser = entityToDTO(user);
        return foundUser;
    }

    @Override
    public UserDTO getUserByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFound("User not found with this User ID!!"));
        UserDTO foundUser = entityToDTO(user);
        return foundUser;
    }

    @Override
    public List<UserDTO> searchUser(String keyword) {
        List<User> users = userRepository.findByNameContaining(keyword);
        List<UserDTO> dtoList = users.stream().map(user -> entityToDTO(user)).collect(Collectors.toList());
        return dtoList;
    }

    private User dtoToEntity(UserDTO userDTO) {
        //  this is manual method to map dto to entity and convert
//        User user = User.builder()
//                .userId(userDTO.getUserId())
//                .name(userDTO.getName())
//                .email(userDTO.getEmail())
//                .password(userDTO.getPassword())
//                .gender(userDTO.getPassword())
//                .imageName(userDTO.getImageName())
//                .about(userDTO.getAbout()).build();

        //this is easy way to map and convert
        return mapper.map(userDTO,User.class);
    }

    private UserDTO entityToDTO(User savedUser) {
//        UserDTO userDto = UserDTO.builder()
//                .userId(savedUser.getUserId())
//                .name(savedUser.getName())
//                .email(savedUser.getEmail())
//                .password(savedUser.getPassword())
//                .gender(savedUser.getGender())
//                .imageName(savedUser.getImageName())
//                .about(savedUser.getAbout())
//                .build();

        return mapper.map(savedUser, UserDTO.class);
    }
}
