package be.kdg.kandoe.controller.rest;

import be.kdg.kandoe.domain.GameSession;
import be.kdg.kandoe.domain.user.User;
import be.kdg.kandoe.service.declaration.AuthenticationHelperService;
import be.kdg.kandoe.service.declaration.GameSessionService;
import be.kdg.kandoe.service.declaration.StorageService;
import be.kdg.kandoe.service.declaration.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@RestController
public class FileRestController {
    private final UserService userService;
    private final AuthenticationHelperService authenticationHelperService;
    private final StorageService storageService;
    private final GameSessionService gameSessionService;

    @Autowired
    public FileRestController(UserService userService, AuthenticationHelperService authenticationHelperService, StorageService storageService, GameSessionService gameSessionService) {
        this.userService = userService;
        this.authenticationHelperService = authenticationHelperService;
        this.storageService = storageService;
        this.gameSessionService = gameSessionService;
    }


    //Upload a profilepicture
    @PostMapping(value = "/api/private/users/{username}/uploadImage")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity uploadProfilePicture(@PathVariable String username, @RequestBody MultipartFile uploadFile, HttpServletRequest request){
//        String usernameFromToken = (String) request.getAttribute("username");
//        User tokenUser = userService.findUserByUsername(usernameFromToken);
//        boolean isAdmin = false;
//        User requestUser = userService.findUserByUsername(username);
//
//
//        for(GrantedAuthority authority : tokenUser.getAuthorities()){
//            if(authority.getAuthority().equalsIgnoreCase("ROLE_ADMIN")){
//                isAdmin = true;
//            }
//        }
//
//        if(!isAdmin && !tokenUser.getUsername().equalsIgnoreCase(username)){
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//        }

        User requestUser = userService.findUserByUsername(username);

        if (!authenticationHelperService.userIsAllowedToAccessResource(request, username)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        };

        if(requestUser == null){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        storageService.store(uploadFile);
        String filename = StringUtils.cleanPath(uploadFile.getOriginalFilename());
        requestUser.setProfilePictureFileName(filename);
        userService.updateUser(requestUser.getUserId(), requestUser);

        userService.updateUser(requestUser.getUserId(), requestUser);
        return ResponseEntity.ok().build();
    }

    //Get a profilepicture
    @GetMapping("/api/private/users/{username}/picture")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<Resource> serveFile(@PathVariable String username, HttpServletRequest request){
//        String usernameFromToken = (String) request.getAttribute("username");
//        User tokenUser = userService.findUserByUsername(usernameFromToken);
//        boolean isAdmin = false;
//        User requestUser = userService.findUserByUsername(username);
//
//
//        for(GrantedAuthority authority : tokenUser.getAuthorities()){
//            if(authority.getAuthority().equalsIgnoreCase("ROLE_ADMIN")){
//                isAdmin = true;
//            }
//        }
//
//        if(!isAdmin && !tokenUser.getUsername().equalsIgnoreCase(username)){
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//        }

        User requestUser = userService.findUserByUsername(username);

        if (!authenticationHelperService.userIsAllowedToAccessResource(request, username)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        };

        if(requestUser == null){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        if(requestUser.getProfilePictureFileName() != null){
            Resource file = storageService.loadAsResource(requestUser.getProfilePictureFileName());
            return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                    "attachment; filename=\"" + file.getFilename() + "\"").body(file);
        }
        else{
            return ResponseEntity.ok().build();
        }
    }

    //Upload a gameSessionImage
    @PostMapping(value = "/api/private/users/{username}/sessions/{id}/uploadImage")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity uploadProfilePicture(@PathVariable String username, @PathVariable Long id, @RequestBody MultipartFile uploadFile, HttpServletRequest request){
//        String t = "";
//
//        String usernameFromToken = (String) request.getAttribute("username");
//        User tokenUser = userService.findUserByUsername(usernameFromToken);
//        boolean isAdmin = false;
//        User requestUser = userService.findUserByUsername(username);
//
//
//        for(GrantedAuthority authority : tokenUser.getAuthorities()){
//            if(authority.getAuthority().equalsIgnoreCase("ROLE_ADMIN")){
//                isAdmin = true;
//            }
//        }
//
//        if(!isAdmin && !tokenUser.getUsername().equalsIgnoreCase(username)){
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//        }

        User requestUser = userService.findUserByUsername(username);

        if (!authenticationHelperService.userIsAllowedToAccessResource(request, username)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        };

        if(requestUser == null){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        storageService.store(uploadFile);
        String filename = StringUtils.cleanPath(uploadFile.getOriginalFilename());

        GameSession gameSession = gameSessionService.getGameSessionWithId(id);
        gameSession.setImage(filename);
        gameSessionService.updateGameSession(gameSession);
        return ResponseEntity.ok().build();
    }

    //Get a gameSessionImage from a session
    @GetMapping("/api/private/users/{username}/sessions/{id}/image")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<Resource> serveFile(@PathVariable String username, @PathVariable Long id, HttpServletRequest request){
//        String usernameFromToken = (String) request.getAttribute("username");
//        User tokenUser = userService.findUserByUsername(usernameFromToken);
//        boolean isAdmin = false;
//        User requestUser = userService.findUserByUsername(username);
//
//
//        for(GrantedAuthority authority : tokenUser.getAuthorities()){
//            if(authority.getAuthority().equalsIgnoreCase("ROLE_ADMIN")){
//                isAdmin = true;
//            }
//        }
//
//        if(!isAdmin && !tokenUser.getUsername().equalsIgnoreCase(username)){
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//        }

        User requestUser = userService.findUserByUsername(username);

        if (!authenticationHelperService.userIsAllowedToAccessResource(request, username)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        };

        if(requestUser == null){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        GameSession gameSession = gameSessionService.getGameSessionWithId(id);

        Resource file = storageService.loadAsResource(gameSession.getImage());
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    //Get a gameSessionImage from a session
    @GetMapping("/api/private/sessions/{id}/image")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<Resource> serveFile(@PathVariable Long id, HttpServletRequest request){
        GameSession gameSession = gameSessionService.getGameSessionWithId(id);

        if(gameSession.getImage() != null){
            Resource file = storageService.loadAsResource(gameSession.getImage());
            return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                    "attachment; filename=\"" + file.getFilename() + "\"").body(file);
        }
        else{
            return ResponseEntity.ok().build();
        }

    }
}
