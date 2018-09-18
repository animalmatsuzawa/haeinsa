# Haeinsa

[![Build Status](https://travis-ci.org/VCNC/haeinsa.svg?branch=master)](https://travis-ci.org/VCNC/haeinsa)

Haeinsa is linearly scalable multi-row, multi-table transaction library for HBase.
Haeinsa uses two-phase locking and optimistic concurrency control for implementing transaction.
The isolation level of transaction is serializable.

## CHANG POINT
これは[aol/haeinsa](https://github.com/aol/haeinsa)からforkしました。  
MapR6.0.1にて動作するようにオリジナルから依存関係を変更しています。  
MapR6.0.1のHBaseAPIには重大なバグがあります。  
ResultScannerをcloseせずにTableをcloseすると無限ループで処理が戻ってこないバグです。  
(MapRのサポートに問い合わせましたが、修正する気はないようです）  
Table.close()する前に必ずResultScanner.close（）を実行してください。  

オリジナルのjunitは、HbaseTestingUtility.startMiniCluster()を使用しているが、本変更では直接MapR Clusterを使用している。  
テスト用のテーブルは"maprfs:/tmp"に配置される  

This forked from [aol/haeinsa](https://github.com/aol/haeinsa).  
I am changing the dependency from the original so that it works with MapR 6.0.1.  

There is a serious bug in the HBase API of MapR 6.0.1.  
If you close the Table without closing the ResultScanner, it is a bug where processing does not come back in an infinite loop.   
(I have contacted MapR support, but it seems I do not feel like modifying it)  
Be sure to execute ResultScanner.close() before doing Table.close().  

The original junit uses HbaseTestingUtility.startMiniCluster(), but in this change, it uses direct MapR Cluster.  
The test table is placed in "maprfs: / tmp".  

## Features

Please see Haeinsa [Wiki] for further information.

- **ACID**: Provides multi-row, multi-table transaction with full ACID senantics.
- **[Linearly scalable]**: Can linearly scale out throughput of transaction as scale out your HBase cluster.
- **[Serializability]**: Provide isolation level of serializability.
- **[Low overhead]**: Relatively low overhead compared to other comparable libraries.
- **Fault-tolerant**: Haeinsa is fault-tolerant against both client and HBase failures.
- **[Easy migration]**: Add transaction feature to your own HBase cluster without any change in HBase cluster except adding lock column family.
- **[Used in practice]**: Haeinsa is used in real service.

## Usage

APIs of Haeinsa is really similar to APIs of HBase. Please see [How to Use] and [API Usage] document for further information.

	HaeinsaTransactionManager tm = new HaeinsaTransactionManager(tablePool);
	HaeinsaTableIface table = tablePool.getTable("test");
	byte[] family = Bytes.toBytes("data");
	byte[] qualifier = Bytes.toBytes("status");

	HaeinsaTransaction tx = tm.begin(); // start transaction

	HaeinsaPut put1 = new HaeinsaPut(Bytes.toBytes("user1"));
	put1.add(family, qualifier, Bytes.toBytes("Hello World!"));
	table.put(tx, put1);

	HaeinsaPut put2 = new HaeinsaPut(Bytes.toBytes("user2"));
	put2.add(family, qualifier, Bytes.toBytes("Linearly Scalable!"));
	table.put(tx, put2);

	tx.commit(); // commit transaction to HBase

## Resources

- [Haeinsa Overview Presentation]: Introducing how Haeina works.
- [Announcing Haeinsa]: Blog post of VCNC Engineering Blog (Korean)
- [Haeinsa: Hbase Transaction Library]: Presentation for Deview Conference (Korean)

## License

	Copyright (C) 2013-2015 VCNC Inc.
	
	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at
	
	        http://www.apache.org/licenses/LICENSE-2.0
	
	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.

[Wiki]: https://github.com/vcnc/haeinsa/wiki
[How to Use]: https://github.com/vcnc/haeinsa/wiki/How-to-Use
[API Usage]: https://github.com/vcnc/haeinsa/wiki/API-Usage
[HBase]: http://hbase.apache.org/
[Serializability]: http://en.wikipedia.org/wiki/Serializability
[Percolator]: http://research.google.com/pubs/pub36726.html
[Haeinsa]: http://en.wikipedia.org/wiki/Haeinsa
[Tripitaka Koreana, or Palman Daejanggyeong]: http://en.wikipedia.org/wiki/Tripitaka_Koreana
[Haeinsa Overview Presentation]: https://speakerdeck.com/vcnc/haeinsa-overview-hbase-transaction-library
[Announcing Haeinsa]: http://engineering.vcnc.co.kr/2013/10/announcing-haeinsa/
[Linearly scalable]: https://github.com/vcnc/haeinsa/wiki/Performance
[Low overhead]: https://github.com/vcnc/haeinsa/wiki/Performance
[Easy Migration]: https://github.com/vcnc/haeinsa/wiki/Migration-from-HBase
[Used in practice]: https://github.com/vcnc/haeinsa/wiki/Use-Case
[Haeinsa: Hbase Transaction Library]: https://speakerdeck.com/vcnc/haeinsa-hbase-transaction-library
