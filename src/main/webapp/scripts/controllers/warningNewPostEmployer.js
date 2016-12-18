angular.module('jobzz')
    .controller('WarningNewPostEmployerCtrl', ['$scope', '$rootScope', function ($scope, $rootScope) {

        $scope.closeDialog = function () {
            $rootScope.panelRef && $rootScope.panelRef.close().then(function () {
                angular.element(document.querySelector('.dialog-button')).focus();
                $rootScope.panelRef.destroy();
            });
        };

    }]);