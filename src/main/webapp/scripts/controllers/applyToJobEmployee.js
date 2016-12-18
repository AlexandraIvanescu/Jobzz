angular.module('jobzz')
    .controller('ApplyToJobEmployeeCtrl', ['$scope', '$rootScope', '$http', '$location', 'jobService', 'CURRENCIES', function ($scope, $rootScope, $http, $location, jobService, CURRENCIES) {
        $scope.job = jobService.getJob();
        $scope.currencies = CURRENCIES;
        $scope.post = {};

        $scope.applyPost = function () {
            $scope.post.employerPosting = $scope.job;

            var req = {
                method: 'POST',
                dataType: 'json',
                url: '/employee/create/post',
                headers: {
                    'Content-Type': 'application/json; charset=utf-8'
                },
                data: $scope.post
            };

            $http(req).then(function (response) {

                if (response.data.isCreated) {
                    $scope.closeDialog();
                    $location.path('/employee/home').replace();
                }

            }, function () {
                console.log('Fail to create post');
            });


        };

        $scope.closeDialog = function () {
            $rootScope.panelRef && $rootScope.panelRef.close().then(function () {
                angular.element(document.querySelector('.dialog-button')).focus();
                $rootScope.panelRef.destroy();
            });
        };
    }]);