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
import org.smartregister.chw.anc.domain.Visit;
import org.smartregister.repository.Repository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class VisitRepositoryTest extends BaseUnitTest {

    private static final String VISIT_ID = "visit_id";
    private static final String VISIT_TYPE = "visit_type";
    private static final String PARENT_VISIT_ID = "parent_visit_id";
    private static final String BASE_ENTITY_ID = "base_entity_id";
    private static final String VISIT_DATE = "visit_date";
    private static final String VISIT_JSON = "visit_json";
    private static final String PRE_PROCESSED = "pre_processed";
    private static final String FORM_SUBMISSION_ID = "form_submission_id";
    private static final String PROCESSED = "processed";
    private static final String UPDATED_AT = "updated_at";
    private static final String CREATED_AT = "created_at";

    @Mock
    private SQLiteDatabase database;
    @Mock
    private Repository repository;

    private VisitRepository visitRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        visitRepository = Mockito.spy(new VisitRepository(repository));
        Mockito.doReturn(database).when(visitRepository).getWritableDatabase();
        Mockito.doReturn(database).when(visitRepository).getReadableDatabase();
    }

    @Test
    public void testCreateTable() {
        SQLiteDatabase database = Mockito.mock(SQLiteDatabase.class);
        VisitRepository.createTable(database);
        // 3 statements are executed
        Mockito.verify(database, Mockito.times(3)).execSQL(Mockito.anyString());
    }

    @Test
    public void testAddVisitCreatesContentToDB() {
        Visit visit = new Visit();
        visit.setUpdatedAt(new Date());
        visit.setCreatedAt(new Date());
        visitRepository.addVisit(visit);
        Mockito.verify(database).insert(Mockito.anyString(), Mockito.isNull(), Mockito.any(ContentValues.class));
    }

    @Test
    public void testGetParentVisitEventID() throws ParseException {
        MatrixCursor cursor = new MatrixCursor(new String[]{"visit_id"});
        cursor.addRow(new Object[]{"345678"});

        Mockito.doReturn(cursor).when(database).rawQuery(Mockito.anyString(), Mockito.any(String[].class));

        Date eventDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse("2019-01-01");
        String id = visitRepository.getParentVisitEventID("1234", "Home Visit", eventDate);

        // expected statement
        String sql = "select visit_id from visits where base_entity_id = ? COLLATE NOCASE and visit_type = ? COLLATE NOCASE and strftime('%Y-%m-%d',visit_date / 1000, 'unixepoch') = ? ";
        String[] parameters = new String[]{"1234", "Home Visit", "2019-01-01"};
        Mockito.verify(database).rawQuery(sql, parameters);
        Assert.assertEquals(id, "345678");
    }

    @Test
    public void testDeleteVisitDeletesTheVisitAndDetails() {
        visitRepository.deleteVisit("123445");

        Mockito.verify(database).delete("visits", "visit_id= ?", new String[]{"123445"});
        Mockito.verify(database).delete("visit_details", "visit_id= ?", new String[]{"123445"});
    }

    @Test
    public void testReadVisits() {
        String[] VISIT_COLUMNS = {
                VISIT_ID, VISIT_TYPE, PARENT_VISIT_ID, BASE_ENTITY_ID,
                VISIT_DATE, VISIT_JSON, PRE_PROCESSED, FORM_SUBMISSION_ID,
                PROCESSED, UPDATED_AT, CREATED_AT
        };

        MatrixCursor cursor = new MatrixCursor(VISIT_COLUMNS);
        cursor.addRow(new Object[]{
                "345678", "type", "0001", "234m234234",
                "1567002594000", null, null, "asdasdasdasd",
                "1", "1567002594000", "1567002594000"
        });

        List<Visit> visits = visitRepository.readVisits(cursor);
        Assert.assertEquals(visits.size(), 1);
    }
}
