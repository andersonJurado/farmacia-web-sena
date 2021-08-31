package org.norn.farmacia;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

class ArchTest {

    @Test
    void servicesAndRepositoriesShouldNotDependOnWebLayer() {
        JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("org.norn.farmacia");

        noClasses()
            .that()
            .resideInAnyPackage("org.norn.farmacia.service..")
            .or()
            .resideInAnyPackage("org.norn.farmacia.repository..")
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage("..org.norn.farmacia.web..")
            .because("Services and repositories should not depend on web layer")
            .check(importedClasses);
    }
}
