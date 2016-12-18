angular.module('jobzz')
    .controller('JobDetailsEmployeeCtrl', ['$scope', '$rootScope', '$http', '$mdPanel', 'jobService', function ($scope, $rootScope, $http, $mdPanel, jobService) {
        $scope.job = jobService.getJob();
        $scope.latlng = [$scope.job.latitude, $scope.job.longitude];

        $scope.$on('mapInitialized', function (event, map) {
            window.setTimeout(function () {
                window.google.maps.event.trigger(map, 'resize');
                map.setCenter(new google.maps.LatLng($scope.job.latitude, $scope.job.longitude));
            }, 100)
        });

        $scope.apply = function () {
            var position = $mdPanel.newPanelPosition()
                .absolute()
                .center();

            var config = {
                attachTo: angular.element(document.body),
                controller: 'ApplyToJobEmployeeCtrl',
                controllerAs: 'ApplyToJobEmployeeCtrl',
                templateUrl: '/views/employee/applyToJob.html',
                hasBackdrop: true,
                panelClass: 'change-post',
                position: position,
                clickOutsideToClose: true,
                escapeToClose: true,
                disableParentScroll: true,
                trapFocus: true
            };

            $mdPanel.open(config).then(function (result) {
                $rootScope.panelRef = result;
            });
        };

    }]);