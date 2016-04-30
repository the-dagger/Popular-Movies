package io.github.the_dagger.movies.data;

import ckm.simple.sql_provider.UpgradeScript;
import ckm.simple.sql_provider.annotation.ProviderConfig;
import ckm.simple.sql_provider.annotation.SimpleSQLConfig;

/**
 * Created by the-dagger on 30/4/16.
 */
@SimpleSQLConfig(
        name = "MovieProvider",
        authority = "io.github.the_dagger.movies",
        database = "movie.db",
        version = 2)

public class MovieProviderConfig implements ProviderConfig {
    @Override
    public UpgradeScript[] getUpdateScripts() {
        return new UpgradeScript[0];
    }
}