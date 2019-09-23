package com.persona5dex;

import androidx.lifecycle.MutableLiveData;
import androidx.annotation.NonNull;

import com.persona5dex.models.PersonaDetailInfo;
import com.persona5dex.models.PersonaShadowDetail;
import com.persona5dex.repositories.PersonaDetailRepository;
import com.persona5dex.viewmodels.PersonaDetailInfoViewModel;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by reche on 1/5/2018.
 */
@RunWith(RobolectricTestRunner.class)
public class PersonaDetailViewModelTest {

    @Test
    public void getShadowNames_sortedInCorrectOrder() {

        PersonaDetailInfo testDetailInfo = getTestDetailInfo();

        MutableLiveData<PersonaDetailInfo> detailInfoData = new MutableLiveData<>();
        detailInfoData.setValue(testDetailInfo);

        PersonaShadowDetail shadowDetailOne = new PersonaShadowDetail("test shadow name", 1);
        PersonaShadowDetail shadowDetailTwo = new PersonaShadowDetail("test shadow name two", 0);

        PersonaShadowDetail[] details = new PersonaShadowDetail[] {shadowDetailOne, shadowDetailTwo };
        MutableLiveData<PersonaShadowDetail[]> shadowData = new MutableLiveData<>();
        shadowData.setValue(details);

        PersonaDetailRepository repository = mock(PersonaDetailRepository.class);
        when(repository.getShadowsForPersona(testDetailInfo.id)).thenReturn(shadowData);
        when(repository.getDetailsForPersona(testDetailInfo.id)).thenReturn(detailInfoData);

        PersonaDetailInfoViewModel viewModel = new PersonaDetailInfoViewModel(repository, testDetailInfo.id);

        viewModel.getShadowsForPersona().observeForever(shadowDisplayName -> {
            assertNotNull(shadowDisplayName);

            String expectedDisplayName = shadowDetailOne.shadowName + "\n" + shadowDetailTwo.shadowName;
            assertEquals(expectedDisplayName, shadowDisplayName);
        });
    }

    @Test
    public void getShadowNames_handlesEmptyShadowNames() {

        PersonaDetailInfo testDetailInfo = getTestDetailInfo();

        MutableLiveData<PersonaDetailInfo> detailInfoData = new MutableLiveData<>();
        detailInfoData.setValue(testDetailInfo);

        MutableLiveData<PersonaShadowDetail[]> shadowData = new MutableLiveData<>();
        shadowData.setValue(new PersonaShadowDetail[0]);

        PersonaDetailRepository repository = mock(PersonaDetailRepository.class);
        when(repository.getShadowsForPersona(testDetailInfo.id)).thenReturn(shadowData);
        when(repository.getDetailsForPersona(testDetailInfo.id)).thenReturn(detailInfoData);

        PersonaDetailInfoViewModel viewModel = new PersonaDetailInfoViewModel(repository, testDetailInfo.id);

        viewModel.getShadowsForPersona().observeForever(shadowDisplayName -> {
            assertNull(shadowDisplayName);
        });
    }

    @Test
    public void getShadowNames_handlesNullShadowNames() {

        PersonaDetailInfo testDetailInfo = getTestDetailInfo();

        MutableLiveData<PersonaDetailInfo> detailInfoData = new MutableLiveData<>();
        detailInfoData.setValue(testDetailInfo);

        MutableLiveData<PersonaShadowDetail[]> shadowData = new MutableLiveData<>();
        shadowData.setValue(null);

        PersonaDetailRepository repository = mock(PersonaDetailRepository.class);
        when(repository.getShadowsForPersona(testDetailInfo.id)).thenReturn(shadowData);
        when(repository.getDetailsForPersona(testDetailInfo.id)).thenReturn(detailInfoData);

        PersonaDetailInfoViewModel viewModel = new PersonaDetailInfoViewModel(repository, testDetailInfo.id);

        viewModel.getShadowsForPersona().observeForever(shadowDisplayName -> {
            assertNull(shadowDisplayName);
        });
    }

    @Test
    public void getPersonaDetails_ReturnsAllProperties() {

        PersonaDetailInfo testDetailInfo = getTestDetailInfo();

        MutableLiveData<PersonaDetailInfo> detailInfoData = new MutableLiveData<>();
        detailInfoData.setValue(testDetailInfo);

        PersonaDetailRepository repository = mock(PersonaDetailRepository.class);
        when(repository.getDetailsForPersona(testDetailInfo.id)).thenReturn(detailInfoData);

        PersonaDetailInfoViewModel viewModel = new PersonaDetailInfoViewModel(repository, testDetailInfo.id);

        viewModel.getDetailsForPersona().observeForever(personaDetailInfo -> {
            assertNotNull(personaDetailInfo);
            assertEquals(testDetailInfo.name, personaDetailInfo.name);
            assertEquals(testDetailInfo.arcanaName, personaDetailInfo.arcanaName);
            assertEquals(testDetailInfo.level, personaDetailInfo.level);
            assertEquals(testDetailInfo.imageUrl, personaDetailInfo.imageUrl);
            assertEquals(testDetailInfo.note, personaDetailInfo.note);
            assertEquals(testDetailInfo.id, personaDetailInfo.id);
        });
    }

    @NonNull
    private static PersonaDetailInfo getTestDetailInfo() {
        PersonaDetailInfo testDetailInfo = new PersonaDetailInfo();
        testDetailInfo.name = "test persona name";
        testDetailInfo.arcanaName = "test arcana";
        testDetailInfo.level = 1;
        testDetailInfo.imageUrl = "test url";
        testDetailInfo.note = "test note";
        testDetailInfo.id = 1;
        return testDetailInfo;
    }
}
