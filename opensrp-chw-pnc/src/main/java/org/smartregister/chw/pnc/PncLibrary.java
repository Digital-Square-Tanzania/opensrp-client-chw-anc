package org.smartregister.chw.pnc;

import org.smartregister.Context;
import org.smartregister.chw.pnc.repository.ProfileRepository;
import org.smartregister.repository.Repository;
import org.smartregister.repository.UniqueIdRepository;
import org.smartregister.sync.ClientProcessorForJava;
import org.smartregister.sync.helper.ECSyncHelper;

import id.zelory.compressor.Compressor;

public class PncLibrary {
    private static PncLibrary instance;


    private int applicationVersion;
    private int databaseVersion;

    private final Context context;
    private final Repository repository;

    private UniqueIdRepository uniqueIdRepository;
    private ECSyncHelper syncHelper;

    private ClientProcessorForJava clientProcessorForJava;
    private Compressor compressor;

    private ProfileRepository profileRepository;

    private PncLibrary(Context contextArg, Repository repositoryArg, int applicationVersion, int databaseVersion) {
        this.context = contextArg;
        this.repository = repositoryArg;
        this.applicationVersion = applicationVersion;
        this.databaseVersion = databaseVersion;

    }

    public static void init(Context context, Repository repository, int applicationVersion, int databaseVersion) {
        if (instance == null) {
            instance = new PncLibrary(context, repository, applicationVersion, databaseVersion);
        }
    }

    public static PncLibrary getInstance() {
        if (instance == null) {
            throw new IllegalStateException(" Instance does not exist!!! Call "
                    + PncLibrary.class.getName()
                    + ".init method in the onCreate method of "
                    + "your Application class ");
        }
        return instance;
    }


    public ProfileRepository profileRepository() {
        if (profileRepository == null) {
            profileRepository = new ProfileRepository(getRepository());
        }
        return profileRepository;
    }

    public Repository getRepository() {
        return repository;
    }

}
