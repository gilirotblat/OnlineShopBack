package gilir.backendfinalpro.validators;


import gilir.backendfinalpro.entity.Item;
import gilir.backendfinalpro.repository.ShopRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class UniqueTitleValidator implements ConstraintValidator<UniqueTitle, String> {


    @Override
    public void initialize(UniqueTitle constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    private final ShopRepository postRepository;

    @Override
    public boolean isValid(String title, ConstraintValidatorContext context) {
        Optional<Item> post = postRepository.findByTitle(title);

        //if user does not exist -> VALID
        return post.isEmpty();
    }
}