package com.transam.druid;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "druid")
public class DruidProperties {
    private String url;
    private String username;
    private String password;
    private String driverClass;

    private int maxActive;
    private int minIdle;
    private int initialSize;
    private boolean testOnBorrow;
    private String filters;
    private Boolean useGlobalDataSourceStat;
    private long maxWait;
    private long timeBetweenEvictionRunsMillis;
    
    
    public long getMaxWait() {
		return maxWait;
	}

	public void setMaxWait(long maxWait) {
		this.maxWait = maxWait;
	}

	public long getTimeBetweenEvictionRunsMillis() {
		return timeBetweenEvictionRunsMillis;
	}

	public void setTimeBetweenEvictionRunsMillis(long timeBetweenEvictionRunsMillis) {
		this.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
	}

	public Boolean getUseGlobalDataSourceStat() {
		return useGlobalDataSourceStat;
	}

	public void setUseGlobalDataSourceStat(Boolean useGlobalDataSourceStat) {
		this.useGlobalDataSourceStat = useGlobalDataSourceStat;
	}

	public String getFilters() {
		return filters;
	}

	public void setFilters(String filters) {
		this.filters = filters;
	}

	public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDriverClass() {
        return driverClass;
    }

    public void setDriverClass(String driverClass) {
        this.driverClass = driverClass;
    }

    public int getMaxActive() {
        return maxActive;
    }

    public void setMaxActive(int maxActive) {
        this.maxActive = maxActive;
    }

    public int getMinIdle() {
        return minIdle;
    }

    public void setMinIdle(int minIdle) {
        this.minIdle = minIdle;
    }

    public int getInitialSize() {
        return initialSize;
    }

    public void setInitialSize(int initialSize) {
        this.initialSize = initialSize;
    }

    public boolean isTestOnBorrow() {
        return testOnBorrow;
    }

    public void setTestOnBorrow(boolean testOnBorrow) {
        this.testOnBorrow = testOnBorrow;
    }
}
