; Licensed to the Apache Software Foundation (ASF) under one
; or more contributor license agreements. See the NOTICE file
; distributed with this work for additional information
; regarding copyright ownership. The ASF licenses this file
; to you under the Apache License, Version 2.0 (the
; "License"); you may not use this file except in compliance
; with the License. You may obtain a copy of the License at
;
; http://www.apache.org/licenses/LICENSE-2.0
;
; Unless required by applicable law or agreed to in writing, software
; distributed under the License is distributed on an "AS IS" BASIS,
; WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
; See the License for the specific language governing permissions and
; limitations under the License.
(ns data-test.file-loader-test
  (:require
   [clojure.test :refer :all]
   [clojure.java.io :as io]
   [schema.core :as s]
   [data-test.file-loader :as sut]))

(deftest should-read-test-data-spec
  (is (= {:simple "test"}
         (sut/read-test-data-spec (io/resource "simple_aero.edn"))))
  (is (= {:to-be-refernced "ref-test", :key1 "ref-test", :key2 "ref-test"}
         (sut/read-test-data-spec (io/resource "tagged_aero.edn"))))
  )

(deftest should-calculate-data-test-spec-file-prefix
  (is (= "data_test/file_loader_test/test_it"
         (sut/data-test-spec-file-prefix ::test-it))))

(deftest should-load-data
  (is (= {:test "data"}
         (sut/load-test-data (sut/data-test-spec-file-prefix ::test-it)))))

(deftest should-throw-exception
  (is (thrown? RuntimeException
               (sut/load-test-data (sut/data-test-spec-file-prefix ::not-existing)))))

(sut/find-data-spec-files (sut/data-test-spec-file-prefix ::test-it))
