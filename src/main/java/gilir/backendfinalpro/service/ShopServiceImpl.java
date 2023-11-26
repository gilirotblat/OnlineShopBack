package gilir.backendfinalpro.service;

import gilir.backendfinalpro.dto.ShopRequestDto;
import gilir.backendfinalpro.dto.ShopResponseDto;
import gilir.backendfinalpro.entity.Item;
import gilir.backendfinalpro.error.ResourceNotFoundException;
import gilir.backendfinalpro.repository.ShopRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ShopServiceImpl implements ShopService {
    private final ModelMapper modelMapper;
    private final ShopRepository shopRepository;

    @Override
    public ShopResponseDto createItem(ShopRequestDto shopRequestDto) {
//convert requestDTO To Entity
        var entity = modelMapper.map(shopRequestDto, Item.class);
//save the entity
        var saved = shopRepository.save(entity);
//convert saved entity to ResponseDto
        return modelMapper.map(saved, ShopResponseDto.class);
    }

    @Override
    public List<ShopResponseDto> getAllItems() {
        return  shopRepository
                .findAll()
                .stream()
                .map(p -> modelMapper.map(p, ShopResponseDto.class))
                .toList();
    }

    @Override
    public ShopResponseDto getItemById(long id) {
        return modelMapper.map(getItemEntity(id),  ShopResponseDto.class);
    }

    @Override
    public List<ShopResponseDto> getItemByCategory(String category) {
        return shopRepository.findByCategory(category)
                .stream()
                .map(item ->  modelMapper.map(item,ShopResponseDto.class))
                .toList();
    }

    //dont copy paste -> reuse
    private Item getItemEntity(long id) {
        return shopRepository.findById(id).orElseThrow(
                () ->  new ResourceNotFoundException("Item", id)
        );
    }

    @Override
    public ShopResponseDto updateItemById(ShopRequestDto dto, long id) {
        Item postFromDb = getItemEntity(id); //assert that id exists

        //update => copy new props from request dto
        if(dto.getTitle() !=null)
            postFromDb.setTitle(dto.getTitle());
        if(dto.getDescription() !=null)
            postFromDb.setDescription(dto.getDescription());
        if(dto.getImg() !=null)
            postFromDb.setImg(dto.getImg());
        if(dto.getPrice() > 0)
            postFromDb.setPrice(dto.getPrice());
        if(dto.getStock() > 0)
            postFromDb.setStock(dto.getStock());
        //save
        var saved = shopRepository.save(postFromDb);

        //return response dto
        return modelMapper.map(saved, ShopResponseDto.class);
    }


    private final Logger logger = LoggerFactory.getLogger(ShopService.class);
    @Override
    public ShopResponseDto deleteItemById(long id) {
        var deleted = shopRepository.findById(id);
        logger.info("Delete item by id {}", id);
        shopRepository.deleteById(id);
        return modelMapper.map(deleted, ShopResponseDto.class);
    }
}
