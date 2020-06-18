Pod::Spec.new do |spec|
    spec.name                     = 'PIARegions'
    spec.version                  = '1.0.0'
    spec.homepage                 = 'https://www.privateinternetaccess.com'
    spec.source            		  = { :git => "https://github.com/pia-foss/mobile-common-regions.git", :tag => "v#{s.version}" }
    spec.authors                  = { "Jose Blaya" => "jose@privateinternetaccess.com", "Juan Docal" => "juan@privateinternetaccess.com" }
    spec.license                  = { :type => "MIT", :file => "LICENSE" }
    spec.summary                  = 'Regions module testing'

    spec.vendored_frameworks      = "regions/build/cocoapods/framework/Regions.framework"
    spec.ios.deployment_target    = "11.0"

    spec.subspec "Core" do |p|
      
        p.source_files = "Core", "iosApp/iosApp/Core/**/*.{h,m,swift}"
        p.private_header_files  = "iosApp/iosApp/Core/**/*.h"
        p.resources         = "iosApp/iosApp/Core/Resources/**/*"
        p.preserve_paths        = "iosApp/iosApp/Core/*.modulemap"
        
        p.pod_target_xcconfig   = { "SWIFT_INCLUDE_PATHS" => "${PODS_TARGET_SRCROOT}/iosApp/iosApp/Core",
                                    "HEADER_SEARCH_PATHS" => "${PODS_TARGET_SRCROOT}/iosApp/iosApp/Core",
                                    "APPLICATION_EXTENSION_API_ONLY" => "YES" }

    end

    spec.pod_target_xcconfig = {
        'KOTLIN_TARGET[sdk=iphonesimulator*]' => 'ios_x64',
        'KOTLIN_TARGET[sdk=iphoneos*]' => 'ios_arm',
        'KOTLIN_TARGET[sdk=watchsimulator*]' => 'watchos_x86',
        'KOTLIN_TARGET[sdk=watchos*]' => 'watchos_arm',
        'KOTLIN_TARGET[sdk=appletvsimulator*]' => 'tvos_x64',
        'KOTLIN_TARGET[sdk=appletvos*]' => 'tvos_arm64',
        'KOTLIN_TARGET[sdk=macosx*]' => 'macos_x64'
    }

    spec.script_phases = [
        {
            :name => 'Build app',
            :execution_position => :before_compile,
            :shell_path => '/bin/sh',
            :script => <<-SCRIPT
                set -ev
                REPO_ROOT="$PODS_TARGET_SRCROOT"
                "$REPO_ROOT/regions/../gradlew" -p "$REPO_ROOT" :regions:syncFramework \
                    -Pkotlin.native.cocoapods.target=$KOTLIN_TARGET \
                    -Pkotlin.native.cocoapods.configuration=$CONFIGURATION \
                    -Pkotlin.native.cocoapods.cflags="$OTHER_CFLAGS" \
                    -Pkotlin.native.cocoapods.paths.headers="$HEADER_SEARCH_PATHS" \
                    -Pkotlin.native.cocoapods.paths.frameworks="$FRAMEWORK_SEARCH_PATHS"
            SCRIPT
        }
    ]
end
