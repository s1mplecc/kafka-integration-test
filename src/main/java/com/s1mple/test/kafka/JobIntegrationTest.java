package com.s1mple.test.kafka;

import com.s1mple.test.kafka.core.exceptions.InvalidIntegrationTestNameException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

public class JobIntegrationTest {
    private static final Logger log = LoggerFactory.getLogger(JobIntegrationTest.class);
    private static final String TEST_POSTFIX = "Test";
    private static ExecutorService executor;
    private static FutureTask<String> jobTask;

    protected final KafkaSuite kafkaSuite;

    public JobIntegrationTest() {
        kafkaSuite = new KafkaSuite(ConfigFactory.load("cloud.yml").kafka());
    }

    @BeforeClass
    public static void start() {
        executor = Executors.newSingleThreadExecutor();
        // add shutdown hook if program is interrupt by manual
        addShutdownHook();
    }

    @AfterClass
    public static void shutdown() {
        executor.shutdown();
        log.info("Job executor has been shutdown.");
    }

    private static void addShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (!jobTask.isCancelled() || !executor.isShutdown()) {
                jobTask.cancel(true);
                executor.shutdown();
                log.info("Job has been shutdown by ShutdownHook. Caused by interrupt the program by manual.");
            }
        }));
    }

    @Before
    public final void setUp() throws Exception {
        String className = this.getClass().getName();

        if (!className.endsWith(TEST_POSTFIX)) {
            throw new InvalidIntegrationTestNameException(
                    String.format("%s, should be named end with \"Test\".", className));
        }

        String jobClassName = className.substring(0, className.lastIndexOf(TEST_POSTFIX));

        try {
            Class clazz = Class.forName(jobClassName);
            AbstractJob job = (AbstractJob) clazz.newInstance();

            job.flows().forEach(flow -> kafkaSuite.fillWith(flow.config().kafka().items()));

            jobTask = createJobTask(job);
            executor.execute(jobTask);
        } catch (ClassNotFoundException ex) {
            throw new InvalidIntegrationTestNameException(
                    String.format("%s not found, should be named like \"xxxJobTest\".", jobClassName), ex);
        }
    }

    private FutureTask<String> createJobTask(Job job) {
        return new FutureTask<>(() -> {
            try {
                log.info("{} is running up. Full class name is {}", job.name(), job.getClass().getName());
                job.run();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, String.format("%s is shutting down.", job.name()));
    }

    @After
    public final void tearDown() {
        jobTask.cancel(true);
    }
}
