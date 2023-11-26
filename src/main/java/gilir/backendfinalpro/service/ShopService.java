package gilir.backendfinalpro.service;

import gilir.backendfinalpro.dto.ShopRequestDto;
import gilir.backendfinalpro.dto.ShopResponseDto;

import java.util.List;

public interface ShopService {
//הצגת מוצרים
    ShopResponseDto createItem(ShopRequestDto postRequestDto);

    List<ShopResponseDto> getAllItems();

    //get post by id:
    ShopResponseDto getItemById(long id);
    List<ShopResponseDto> getItemByCategory(String category);

    ShopResponseDto updateItemById(ShopRequestDto dto, long id);

    ShopResponseDto deleteItemById(long id);


}
