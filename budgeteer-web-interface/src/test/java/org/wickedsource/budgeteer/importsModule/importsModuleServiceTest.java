package org.wickedsource.budgeteer.importsModule;

import de.olivergierke.moduliths.model.test.ModuleTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.wickedsource.budgeteer.importsModule.internal.persistence.ImportEntity;
import org.wickedsource.budgeteer.importsModule.internal.service.Import;
import org.wickedsource.budgeteer.persistence.record.WorkRecordRepository;
import org.wickedsource.budgeteer.service.ServiceTestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ModuleTest(mode = ModuleTest.BootstrapMode.ALL_DEPENDENCIES)
public class importsModuleServiceTest extends ServiceTestTemplate {

    @Autowired
    private ImportRepository importRepository;

    @Autowired
    private WorkRecordRepository workRecordRepository;

    @Autowired
    private ImportService importService;

    @Test
    void testLoadImports() {
        when(importRepository.findByProjectId(1L)).thenReturn(Collections.singletonList(createImportEntity()));
        List<Import> imports = importService.loadImports(1L);
        Assertions.assertEquals(1, imports.size());
        Assertions.assertEquals("TestImport", imports.get(0).getImportType());
    }

    @Test
    void testDeleteImport(){
        importService.deleteImport(1L);
        verify(importRepository, times(1)).deleteById(1L);
        verify(workRecordRepository, times(1)).deleteByImport(1L);
    }

    private ImportEntity createImportEntity() {
        ImportEntity entity = new ImportEntity();
        entity.setEndDate(new Date());
        entity.setStartDate(new Date());
        entity.setImportType("TestImport");
        entity.setId(1L);
        return entity;
    }
}
