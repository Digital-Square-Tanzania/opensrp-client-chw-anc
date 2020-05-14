package org.smartregister.chw.anc.domain;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.smartregister.chw.anc.BaseUnitTest;

import java.util.Date;

public class VisitDetailsTest extends BaseUnitTest {

    private VisitDetail visitDetail = new VisitDetail();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetAndSetVisitDetailsId() {
        String visitDetailId = "2121";
        visitDetail.setVisitDetailsId(visitDetailId);
        Assert.assertEquals(visitDetail.getVisitDetailsId(), visitDetailId);
    }

    @Test
    public void testGetAndSetVisitId() {
        String visitId = "343563";
        visitDetail.setVisitId(visitId);
        Assert.assertEquals(visitDetail.getVisitId(), visitId);
    }

    @Test
    public void testGetAndSetBaseEntityId() {
        String baseEntityId = "12234";
        visitDetail.setBaseEntityId(baseEntityId);
        Assert.assertEquals(visitDetail.getBaseEntityId(), baseEntityId);
    }

    @Test
    public void testGetAndGetVisitKey() {
        String visitKey = "7387y87389";
        visitDetail.setVisitKey(visitKey);
        Assert.assertEquals(visitDetail.getVisitKey(), visitKey);
    }

    @Test
    public void testGetAndSetParentCode() {
        String parentCode = "codePara1";
        visitDetail.setParentCode(parentCode);
        Assert.assertEquals(visitDetail.getParentCode(), parentCode);
    }

    @Test
    public void testGetAndSetDetails() {
        String details = "details";
        visitDetail.setDetails(details);
        Assert.assertEquals(visitDetail.getDetails(), details);
    }

    @Test
    public void testGetAndSetJsonDetails() {
        String jsonDetails = "jsonDetails";
        visitDetail.setJsonDetails(jsonDetails);
        Assert.assertEquals(visitDetail.getJsonDetails(), jsonDetails);
    }

    @Test
    public void testGetPreProcessedJson() {
        String preProcessedJson = "preProcessedJson";
        visitDetail.setPreProcessedJson(preProcessedJson);
        Assert.assertEquals(visitDetail.getPreProcessedJson(), preProcessedJson);
    }

    @Test
    public void testGetAndSetPreProcessedType() {
        String preProcessedType = "preProcessedType";
        visitDetail.setPreProcessedType(preProcessedType);
        Assert.assertEquals(visitDetail.getPreProcessedType(), preProcessedType);
    }

    @Test
    public void testGetAndSetHumanReadable() {
        String humanReadable = "humanReadable";
        visitDetail.setHumanReadable(humanReadable);
        Assert.assertEquals(visitDetail.getHumanReadable(), humanReadable);
    }

    @Test
    public void testGetAndSetProcessed() {
        visitDetail.setProcessed(true);
        Assert.assertEquals(visitDetail.getProcessed(), true);
    }

    @Test
    public void testGetAndSetUpdatedAt() {
        Date date = new Date();
        visitDetail.setUpdatedAt(date);
        Assert.assertEquals(visitDetail.getUpdatedAt(), date);
    }

    @Test
    public void testGetAndSetCreatedAt() {
        Date date = new Date();
        visitDetail.setCreatedAt(date);
        Assert.assertEquals(visitDetail.getCreatedAt(), date);
    }

}

