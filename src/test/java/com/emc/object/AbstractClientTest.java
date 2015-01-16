package com.emc.object;

import com.emc.test.util.Concurrent;
import com.emc.test.util.ConcurrentJunitRunner;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;

import java.util.concurrent.atomic.AtomicInteger;

@RunWith(ConcurrentJunitRunner.class)
@Concurrent
public abstract class AbstractClientTest {
    private static final Logger l4j = Logger.getLogger(AbstractClientTest.class);
    private static final AtomicInteger counter = new AtomicInteger(0);
    private final ThreadLocal<String> testBucket = new ThreadLocal<>();

    /**
     * Implement to initialize the object client to be used for each test. Each subclass must keep a reference to the
     * client.
     */
    protected abstract void initClient() throws Exception;

    /**
     * Implement to create an arbitrary bucket or directory. Do not do anything if the
     * bucket/directory already exists.
     */
    protected abstract void createBucket(String bucketName) throws Exception;

    /**
     * Ditto for deleting a bucket/directory and all of its contents
     */
    protected abstract void cleanUpBucket(String bucketName) throws Exception;

    /**
     * Override to provide a different bucket prefix for each subclass.
     */
    protected String getTestBucketPrefix() {
        return "client-tests";
    }

    /**
     * Always call this method to get the bucket/directory to put object in for each test.
     *
     * @return a unique existing bucket for a particular test. will be cleaned up after the test automatically.
     */
    protected final String getTestBucket() {
        return testBucket.get();
    }

    @Before
    public final void initTestBucket() throws Exception {
        l4j.info("initializing client");
        initClient();

        testBucket.set(getTestBucketPrefix() + "-" + counter.incrementAndGet());
        l4j.info("creating test bucket " + getTestBucket());
        createBucket(getTestBucket());
    }

    @After
    public final void destroyTestBucket() throws Exception {
        l4j.info("cleaning up bucket " + getTestBucket());
        cleanUpBucket(getTestBucket());
    }
}
