
const path = require('path');
const babelEnvPreset = ['env', {
    "targets": {
        "browsers": ["last 2 versions"]
    }
}];

const babelLoader = {
  loader: 'babel-loader',
  options: {
    presets: [babelEnvPreset, 'react'],
    plugins: ['transform-object-rest-spread', 'transform-class-properties', "babel-root-slash-import"]
  }
};

const webpack = require('webpack');

module.exports = {
    entry: ['babel-polyfill', path.resolve(__dirname, 'src/index.tsx')],
    output: {
        filename: 'bundle.js',
        path: path.join(__dirname, '/../public')
    },
    devtool: 'source-map',
    resolve: {
        extensions: ['.webpack.js', '.web.js', '.json', '.mjs', '.ts', '.tsx', '.js'],
        alias: {
            constants: path.resolve(__dirname, 'src/constants'),
            state: path.resolve(__dirname, 'src/graphql'),
            utils: path.resolve(__dirname, 'src/utils'),
            components: path.resolve(__dirname, 'src/components')
        }
    },
    module: {
        rules: [
            {
                test: /\.mjs$/,
                include: /node_modules/,
                type: "javascript/auto",
            },
            {
                test: /\.tsx?$/,
                exclude: [/node_modules/],
                use: [babelLoader, 'ts-loader'],
                include: path.resolve('src')
            },
            {
                test: /\.jsx?$/,
                exclude: [/node_modules/],
                use: [babelLoader]
            },
            {
                test: /\.css$/,
                use: [
                    'style-loader',
                    {
                        loader: 'css-loader',
                        options: {
                            importLoaders: 1,
                            modules: true,
                            localIdentName: '[local]_[hash:base64:5]', // Add naming scheme
                        },
                    }
                ],
            },
            {
                test: /\.scss$/,
                use: [
                        {
                            loader: "style-loader" // creates style nodes from JS strings
                        },
                        {
                            loader: "css-loader" // translates CSS into CommonJS
                        },
                        {
                            loader: "sass-loader" // compiles Sass to CSS
                        }
                     ]
            },
            {
                test: /\.png$/,
                use: 'url-loader'
            }
        ]
    },
    plugins: [
        new webpack.LoaderOptionsPlugin({
            options: {
                exclude: [
                    path.resolve(__dirname, 'src/__test__')
                ],
                context: __dirname,
                watchOption: {
                    poll: true
                }
            }
        })
    ]
};