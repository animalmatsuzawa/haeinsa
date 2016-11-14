/**
 * Copyright (C) 2013-2015 VCNC Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package kr.co.vcnc.haeinsa.thrift;

import kr.co.vcnc.haeinsa.thrift.generated.TMutation;
import kr.co.vcnc.haeinsa.thrift.generated.TMutationType;
import kr.co.vcnc.haeinsa.thrift.generated.TRemove;
import kr.co.vcnc.haeinsa.thrift.generated.TRowLock;
import kr.co.vcnc.haeinsa.thrift.generated.TRowLockState;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.thrift.TBaseHelper;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class HaeinsaTRowLockSerializeTest {

  @Test
  public void serializeTRowLockTest() throws IOException {
    TRowLock lock = new TRowLock();
    TMutation mutation = new TMutation();
    TRemove remove = new TRemove();
    remove.addToRemoveFamilies(ByteBuffer.wrap(Bytes.toBytes("f1")));
    mutation.setRemove(remove);
    mutation.setType(TMutationType.REMOVE);
    lock.addToMutations(mutation);
    lock.setState(TRowLockState.STABLE);

    byte[] lockBytes = TRowLocks.serialize(lock);

    lock = TRowLocks.deserialize(lockBytes);
    Assert.assertEquals(lock.getMutations().size(), 1);
    Assert.assertEquals(lock.getMutations().get(0).getType(), TMutationType.REMOVE);
    Assert.assertEquals(lock.getMutations().get(0).getRemove().getRemoveFamilies().size(), 1);
    byte[] readFamily = TBaseHelper.rightSize(lock.getMutations().get(0).getRemove().getRemoveFamilies().get(0)).array();
    byte[] writtenFamily = Bytes.toBytes("f1");
    Assert.assertTrue(Bytes.compareTo(readFamily, writtenFamily) == 0);
  }
}
