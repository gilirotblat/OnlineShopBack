package gilir.backendfinalpro.controller;


import gilir.backendfinalpro.dto.ShopRequestDto;
import gilir.backendfinalpro.dto.ShopResponseDto;
import gilir.backendfinalpro.service.ShopService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
//@Tag(name = "Shop controller", description = "All the items")
@RequestMapping("/api/v1/shop")
@RequiredArgsConstructor

public class ShopController {
    //props:
    private final ShopService shopService;


    private final Logger logger = LoggerFactory.getLogger(ShopController.class);
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ShopResponseDto> createItem(
            @RequestBody @Valid ShopRequestDto dto, UriComponentsBuilder uriBuilder) {

        var saved = shopService.createItem(dto);

        var uri = uriBuilder.path("/api/v1/shop/{id}").buildAndExpand(saved.getId()).toUri();

        return ResponseEntity.created(uri).body(saved);
    }

    @GetMapping
    @Operation(summary = "Get all the items")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(
                                    schema = @Schema(implementation = ShopResponseDto.class)
                            ))),
            @ApiResponse(
                    responseCode = "401",
                    content = @Content(mediaType = "application/json"),
                    description = "Unauthorized"
            )
    })
    public ResponseEntity<List<ShopResponseDto>> getAllItems() {
        return ResponseEntity.ok(shopService.getAllItems());
    }


    @Operation(summary = "Get a item by its category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ShopResponseDto.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Item not found",
                    content = @Content)})
    @GetMapping("/category/{category}")
    public ResponseEntity<List<ShopResponseDto>> getItemByCategory(
            @Valid @NotNull @PathVariable String category) {
        return ResponseEntity.ok(shopService.getItemByCategory(category));
    }



    //getItemById
    @Operation(summary = "Get a item by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ShopResponseDto.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Item not found",
                    content = @Content)})
    @GetMapping("/{id}")
    public ResponseEntity<ShopResponseDto> getItemById(
            @Valid @NotNull @PathVariable long id) {
        return ResponseEntity.ok(shopService.getItemById(id));
    }

   //updateItemById
    @PutMapping("/{id}")
   @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ShopResponseDto> updateItemById(
            @Valid @NotNull @PathVariable long id,
            @Valid @RequestBody ShopRequestDto dto) {
        return ResponseEntity.ok(shopService.updateItemById(dto, id));
    }

    //DELETE
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ShopResponseDto> deleteItemById(
            @Valid @NotNull @PathVariable long id)  {
        logger.info("Delete item by id {}",id);
        return ResponseEntity.ok(shopService.deleteItemById(id));
    }


}
