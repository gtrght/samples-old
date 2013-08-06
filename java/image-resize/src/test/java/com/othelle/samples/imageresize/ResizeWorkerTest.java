package com.othelle.samples.imageresize;

import org.hamcrest.Matchers;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;

/**
 * author: v.vlasov
 */
public class ResizeWorkerTest {


    @Test
    public void testSwap() {
        ResizeWorker worker = new ResizeWorker();
        worker.setExpectedWidth(100);
        worker.setExpectedHeight(200);

        worker.swapDimensions();

        assertThat(worker.getExpectedHeight(), Matchers.equalTo(100));
        assertThat(worker.getExpectedWidth(), Matchers.equalTo(200));
    }
}
