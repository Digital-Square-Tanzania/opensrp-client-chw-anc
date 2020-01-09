package org.smartregister.chw.anc.repository;

import android.content.ContentValues;

import net.sqlcipher.MatrixCursor;
import net.sqlcipher.database.SQLiteDatabase;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.smartregister.chw.anc.BaseUnitTest;
import org.smartregister.chw.anc.TestApplication;
import org.smartregister.chw.anc.domain.VisitDetail;
import org.smartregister.repository.Repository;
import org.smartregister.view.activity.DrishtiApplication;

import java.util.Date;
import java.util.List;

public class VisitDetailsRepositoryTest extends BaseUnitTest {

    public static final String VISIT_DETAILS_TABLE = "visit_details";
    private static final String VISIT_DETAILS_ID = "visit_details_id";
    private static final String VISIT_ID = "visit_id";
    private static final String VISIT_KEY = "visit_key";
    private static final String PARENT_CODE = "parent_code";
    private static final String DETAILS = "details";
    private static final String HUMAN_READABLE = "human_readable_details";
    private static final String JSON_DETAILS = "json_details";
    private static final String PRE_PROCESSED_JSON = "preprocessed_details";
    private static final String PRE_PROCESSED_TYPE = "preprocessed_type";
    private static final String PROCESSED = "processed";
    private static final String UPDATED_AT = "updated_at";
    private static final String CREATED_AT = "created_at";

    @Mock
    private SQLiteDatabase database;

    @Mock
    private Repository repository;

    @Mock
    private DrishtiApplication application;

    private VisitDetailsRepository visitDetailsRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        Mockito.when(application.getRepository()).thenReturn(repository);
        TestApplication.setInstance(application);

        visitDetailsRepository = Mockito.spy(new VisitDetailsRepository());
        Mockito.doReturn(database).when(visitDetailsRepository).getWritableDatabase();
        Mockito.doReturn(database).when(visitDetailsRepository).getReadableDatabase();
    }

    @Test
    public void testCreateTable() {
        SQLiteDatabase database = Mockito.mock(SQLiteDatabase.class);
        VisitDetailsRepository.createTable(database);
        // 3 statements are executed
        Mockito.verify(database, Mockito.times(2)).execSQL(Mockito.anyString());
    }

    @Test
    public void testAddVisitDetails() {
        VisitDetail visitDetail = new VisitDetail();
        visitDetail.setUpdatedAt(new Date());
        visitDetail.setCreatedAt(new Date());
        visitDetail.setProcessed(false);
        visitDetailsRepository.addVisitDetails(visitDetail);
        Mockito.verify(database).insert(Mockito.anyString(), Mockito.isNull(), Mockito.any(ContentValues.class));
    }

    @Test
    public void testCompleteProcessing() {

        // updates the database. Confirm the database
        visitDetailsRepository.completeProcessing("1234567");
        Mockito.verify(database).update(Mockito.anyString(), Mockito.any(ContentValues.class), Mockito.anyString(), Mockito.any(String[].class));
    }

    @Test
    public void testReadVisitDetails() {
        String[] VISIT_DETAILS_COLUMNS = {
                VISIT_ID, VISIT_KEY, PARENT_CODE, VISIT_DETAILS_ID,
                HUMAN_READABLE, JSON_DETAILS, PRE_PROCESSED_JSON, PRE_PROCESSED_TYPE,
                DETAILS, PROCESSED, UPDATED_AT, CREATED_AT
        };


        MatrixCursor cursor = new MatrixCursor(VISIT_DETAILS_COLUMNS);
        cursor.addRow(new Object[]{
                "345678", "key", "parent_code", "details_id",
                "hr", null, null, "service",
                "detaills", "1", "1567002594000", "1567002594000"
        });

        List<VisitDetail> visitDetails = visitDetailsRepository.readVisitDetails(cursor);
        Assert.assertEquals(visitDetails.size(), 1);
    }
}
