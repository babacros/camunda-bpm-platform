/*
 * Copyright Camunda Services GmbH and/or licensed to Camunda Services GmbH
 * under one or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information regarding copyright
 * ownership. Camunda licenses this file to you under the Apache License,
 * Version 2.0; you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

module.exports = `<span cam-widget-inline-field
  class="set-value"
  type="{{type}}"
  change="onChange()"
  value="property">
  <span ng-show="property == null" class="null-value">{{ 'TIME_TO_LIVE_NULL' | translate }}</span>
  <span ng-show="property != null">
    {{format({property: property}) || property}}
    <button type="button" class="btn btn-link btn-xs" ng-click="remove($event)">
      <span class="glyphicon glyphicon-remove" aria-hidden="true"></span>
    </button>
  </span>
</span>
`;
