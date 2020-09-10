# opensrp-client-chw-anc
[![Build Status](https://travis-ci.org/OpenSRP/opensrp-client-chw-anc.svg?branch=master)](https://travis-ci.org/OpenSRP/opensrp-client-chw-anc)
[![Coverage Status](https://coveralls.io/repos/github/OpenSRP/opensrp-client-chw-anc/badge.svg?branch=master)](https://coveralls.io/github/OpenSRP/opensrp-client-chw-anc?branch=master)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/957ae1b75713494dbdaa94aceb6a222b)](https://app.codacy.com/app/OpenSRP/opensrp-client-chw-anc?utm_source=github.com&utm_medium=referral&utm_content=OpenSRP/opensrp-client-chw-anc&utm_campaign=Badge_Grade_Settings)

This library contains content and logic for antenatal care (ANC) and postnatal care (PNC) workflows for community health workers (CHWs). CHWs can register pregnant women to the ANC register in the app and record the high priority ANC services provided to them either at home or in the health center, including ANC visits, tetanus vaccination, malaria prophylaxis, testing, etc. The outcome of the pregnancy is also recorded, which includes a birth registration component for any children born. The mother plus baby pair are then transferred to the PNC register, where the CHW can track the PNC and neonatal services provided to them.

## Getting Started
These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.

## Prerequisites
[Tools and Frameworks Setup](https://smartregister.atlassian.net/wiki/spaces/Documentation/pages/6619207/Tools+and+Frameworks+Setup)

## Development setup

### Steps to set up
[OpenSRP android client app build](https://smartregister.atlassian.net/wiki/spaces/Documentation/pages/6619236/OpenSRP+App+Build)

### Running the tests

[Android client unit tests](https://smartregister.atlassian.net/wiki/spaces/Documentation/pages/65570428/OpenSRP+Client)

## Deployment
[Production releases](https://smartregister.atlassian.net/wiki/spaces/Documentation/pages/1141866503/How+to+create+a+release+APK)

## Features

|Function                            |Feature                                                   |Dependencies                                   |
|------------------------------------|----------------------------------------------------------|-----------------------------------------------|
|Client Identification & Registration|Register a pregnant woman                                 |opensrp-client-core, opensrp-client-native-form|
|Client Management                   |Record the woman's pregnancy outcome                      |opensrp-client-core, opensrp-client-native-form|
|Service Delivery Support            |Monthly home visit schedules                              |opensrp-client-native-form (rules engine)      |
|Service Delivery Support            |Schedules for high priority ANC services                  |opensrp-client-native-form (rules engine)      |
|Client Management                   |Record services provided during monthly home visit        |opensrp-client-native-form (rules engine)      |
|Service Delivery Support            |Call the woman                                            |                                               |
|Client Identification & Registration|Register the woman for PNC services after delivery outcome|opensrp-client-core, opensrp-client-native-form|
|Service Delivery Support            |PNC visit schedules - at home and at health facility      |opensrp-client-native-form (rules engine)      |
|Service Delivery Support            |Schedules for high priority PNC services                  |opensrp-client-native-form (rules engine)      |
|Client Management                   |Record services provided during PNC home visit            |opensrp-client-native-form (rules engine)      |

## Versioning
We use SemVer for versioning. For the versions available, see the tags on this repository.
For more details check out <https://semver.org/>

## Authors/Team
-   The OpenSRP team
-   See the list of contributors who participated in this project from the [Contributors](../../graphs/contributors) link

## Contributing
[Contribution guidelines](https://smartregister.atlassian.net/wiki/spaces/Documentation/pages/6619193/OpenSRP+Developer+s+Guide)

## Documentation
Wiki [OpenSRP Documentation](https://smartregister.atlassian.net/wiki/spaces/Documentation)

## Support
Email: <mailto:support@ona.io>
Slack workspace: <opensrp.slack.com>

## License
This project is licensed under the Apache 2.0 License - see the LICENSE.md file for details
